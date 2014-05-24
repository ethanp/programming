package InheritanceTest

/**
 * Ethan Petuchowski
 * 5/23/14
 */

class Base[T](val elem: T, val nextNode: Base[T])

class Ord[U <% Ordered[U]](override val elem: U, override val nextNode: Ord[U]) extends Base[U](elem, nextNode) with Ordered[Ord[U]] {
  override def compare(that: Ord[U]) = this.elem compare that.elem
}

object Run extends App {
  val a = new Ord(3, null)
  val b = new Ord(4, null)
  println(a < b)
}
