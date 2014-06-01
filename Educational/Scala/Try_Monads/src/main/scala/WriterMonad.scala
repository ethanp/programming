/**
 * Ethan Petuchowski
 * 5/27/14
 *
 * This is a Scala translation of the Javascript writer monad
 * example on the Monad wikipedia page
 * http://en.wikipedia.org/wiki/Monad_(functional_programming)
 *
 * This was a very useful exercise.
 */

// the bind() method from the example
case class Writer[A](value: A, log: String) {
  def flatMap[B](f: (A) => Writer[B]): Writer[B] = {
    f(value) match {
      case Writer(newVal, newStr) =>
        Writer(newVal, log + newStr)
    }
  }
}

// the unit() method from the example
object Writer {
  def apply[A](a: A): Writer[A] = Writer(a, "")
}

object Run extends App {

  def maybePlus(a: Option[Int], b: Option[Int]): Option[Int] =
    for (ia <- a; ib <- b) yield ia + ib

  println("------- Maybe Plus ---------")
  println(maybePlus(Some(3), Some(4)))
  println(maybePlus(Some(3), None))

  println("\n------- Writer Monad ---------")
  println(
    Writer(2)
      .flatMap(x => Writer(x*x, "squared."))
      .flatMap(x => Writer(x + 3, "plus threed."))
  )
}
