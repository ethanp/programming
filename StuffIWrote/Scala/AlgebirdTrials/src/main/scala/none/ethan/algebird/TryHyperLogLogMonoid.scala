package none.ethan.algebird

import com.twitter.algebird._
import HyperLogLog._  // explicitly import implicit conversions

object TryHyperLogLogMonoid extends App {
  import HLLMUtilityFuncs._
  none.ethan.algebird.HyperLogLogMonoidTests.runTests

  // approximate number of 1000 users
  val userSet = (1 to 1000).toSet
  printf(
    "Exact would be %d, but HLL says ~%d users in this set\n",
    userSet.size,
    approxCount(userSet).toInt
  )

  // intersect with a "similar" set sum it up and see what happens
  val userSet2 = (1 to 2000 by 2).toSet
  val userSets = Seq[Set[Int]](userSet,userSet2)
  printf(
    "Exact would be %d, but HLL says ~%d users in this set-intersection\n",
    userSet.intersect(userSet2).size,
    approxIntersect(userSets).toInt
  )
}
/* these functions come from com.twitter.algebird.HyperLogLogTest
 * not my code, etc. */
object HLLMUtilityFuncs {

  def exactCount[T](it : Iterable[T]) : Int = it.toSet.size

  def approxCount[T <% Array[Byte]](it : Iterable[T]) = {
    val hll = new HyperLogLogMonoid(12)
    hll.sizeOf(hll.sum(it.map { hll(_) })).estimate.toDouble
  }

  def aveErrorOf(bits : Int) : Double = 1.04/scala.math.sqrt(1 << bits)

  def exactIntersect[T](it : Seq[Iterable[T]]) : Int =
    it.foldLeft(Set[T]()) { (old, newS) => old ++ (newS.toSet) }.size

  def approxIntersect[T <% Array[Byte]](it : Seq[Iterable[T]]) : Double = {
    val hll = new HyperLogLogMonoid(12)
    //Map each iterable to a HLL instance:
    val seqHlls = it.map { iter => hll.sum(iter.view.map { hll(_) }) }
    hll.intersectionSize(seqHlls).estimate.toDouble
  }
}

/* these tests from from com.twitter.algebird.HyperLogLogTest
 * to make sure all is functioning normally; not my code, etc. */
object HyperLogLogMonoidTests {
  import HLLMUtilityFuncs._

  val r = new java.util.Random

  def test : Boolean = {
    val data = (0 to 10000).map { i => r.nextInt(1000) }
    val exact = exactCount(data).toDouble
    val actualVariance = scala.math.abs(exact - approxCount(data)) / exact
    val reasonableVariance = (3.5 * aveErrorOf(12))
    actualVariance < reasonableVariance
  }

  def testLong : Boolean = {
    val data = (0 to 10000).map { i => r.nextLong }
    val exact = exactCount(data).toDouble
    val actualVariance = scala.math.abs(exact - approxCount(data)) / exact
    val reasonableVariance = (3.5 * aveErrorOf(12))
    actualVariance < reasonableVariance
  }

  def testLongIntersection(sets : Int) : Boolean = {
    val data : Seq[Iterable[Int]] = (0 until sets).map { idx =>
      (0 to 1000).map { i => r.nextInt(100) }
    }.toSeq
    val exact = exactIntersect(data)
    val errorMult = scala.math.pow(2.0, sets) - 1.0
    val actualVariance = scala.math.abs(exact - approxIntersect(data)) / exact
    val reasonableVariance = (errorMult * aveErrorOf(12))
    actualVariance < reasonableVariance
  }

  def runTests {
    if (test && testLong && testLongIntersection(5))
      println("tests Pass\n")
    else {
      println("at least one test FAILED!@");
      sys.exit(1)
    }
  }
}
