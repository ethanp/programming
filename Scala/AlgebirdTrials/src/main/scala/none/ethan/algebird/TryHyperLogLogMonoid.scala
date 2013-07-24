package none.ethan.algebird

import com.twitter.algebird._
import HyperLogLog._  // explicitly import implicit conversions

object TryHyperLogLogMonoid extends App {
  val h = new HyperLogLogMonoid(12)
  h(12)
  println("ehllo")
}
