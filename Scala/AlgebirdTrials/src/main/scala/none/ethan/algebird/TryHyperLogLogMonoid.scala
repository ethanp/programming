package none.ethan.algebird

import com.twitter.algebird._

object TryHyperLogLogMonoid extends App {
  import HyperLogLog._  // explicitly import implicit conversions

  /* copy in some useful stuff from the HLL test-file */
  val r = new java.util.Random

  def exactCount[T](it : Iterable[T]) : Int = it.toSet.size
  def approxCount[T <% Array[Byte]](bits : Int, it : Iterable[T]) = {
    val hll = new HyperLogLogMonoid(bits)
    hll.sizeOf(hll.sum(it.map { hll(_) })).estimate.toDouble
  }

  def aveErrorOf(bits : Int) : Double = 1.04/scala.math.sqrt(1 << bits)

  def exactIntersect[T](it : Seq[Iterable[T]]) : Int = {
    it.foldLeft(Set[T]()) { (old, newS) => old ++ (newS.toSet) }.size
  }
  def approxIntersect[T <% Array[Byte]](bits : Int, it : Seq[Iterable[T]]) : Double = {
    val hll = new HyperLogLogMonoid(bits)
    //Map each iterable to a HLL instance:
    val seqHlls = it.map { iter => hll.sum(iter.view.map { hll(_) }) }
    hll.intersectionSize(seqHlls).estimate.toDouble
  }

  def test(bits : Int) {
    val data = (0 to 10000).map { i => r.nextInt(1000) }
    val exact = exactCount(data).toDouble
    val res = scala.math.abs(exact - approxCount(bits, data)) / exact - (3.5 * aveErrorOf(bits))
    println(res)
  }
  def testLong(bits : Int) {
    val data = (0 to 10000).map { i => r.nextLong }
    val exact = exactCount(data).toDouble
    val res = scala.math.abs(exact - approxCount(bits, data)) / exact - (3.5 * aveErrorOf(bits))
    println(res)
  }
  def testLongIntersection(bits : Int, sets : Int) {
    val data : Seq[Iterable[Int]] = (0 until sets).map { idx =>
      (0 to 1000).map { i => r.nextInt(100) }
    }.toSeq
    val exact = exactIntersect(data)
    val errorMult = scala.math.pow(2.0, sets) - 1.0
    val res = scala.math.abs(exact - approxIntersect(bits, data)) / exact - (errorMult * aveErrorOf(bits))
    println(res)
  }

  val userSet = (1 to 1000).toSet
  val userSet2 = (1 to 2000 by 2).toSet
  val userSets = Seq[Set[Int]](userSet,userSet2)

  val approxUsers =
    userSets map {
      aUserSet => {
        val h = new HyperLogLogMonoid(12)
        aUserSet map {
          userID =>
            h(userID)
        }
      }
    }

  test(12)
  testLong(12)
  testLongIntersection(12, 5)

  // add in a "similar" set sum it up and see what happens
}
