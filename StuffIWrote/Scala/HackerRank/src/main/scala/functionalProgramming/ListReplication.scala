package functionalProgramming

/**
 * Ethan Petuchowski
 * 7/13/14
 */
object Solution extends App {
  def f(num:Int,arr:List[Int]):List[Int] =
    arr.foldLeft(List.empty[Int]) {
      (list, elem) => list ++ List.fill(num)(elem)
    }
  def displayResult(arr:List[Int]) = println(f(arr(0).toInt,arr.drop(1)).map(_.toString).mkString("\n"))
  displayResult(io.Source.stdin.getLines.toList.map(_.trim).map(_.toInt))
}
