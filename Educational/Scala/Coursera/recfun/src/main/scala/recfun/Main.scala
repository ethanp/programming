package recfun
import common._

object Main {
  def main(args: Array[String]) {
    println("Pascal's Triangle")
    for (row <- 0 to 10) {
      for (col <- 0 to row)
        print(pascal(col, row) + " ")
      println()
    }
  }

  /**
   * Exercise 1
   */
  def pascal(c: Int, r: Int): Int = {
    if (c == 0 || c == r) 1
    else pascal(c-1, r-1) + pascal(c, r-1)
  }

  /**
   * Exercise 2
   */
  def balance(chars: List[Char]): Boolean = {
    def iBal(open: Int, chars: List[Char]): Boolean = {
      if (chars.isEmpty)
        open == 0
      else if (chars.head == ')') {
        if (open == 0) false
        else iBal(open-1, chars.tail)
      }
      else if (chars.head == '(')
        iBal(open+1, chars.tail)
      else
        iBal(open, chars.tail)
    }
    iBal(0, chars)
  }

  /**
   * Exercise 3
   */
  def countChange(money: Int, coins: List[Int]): Int = {

    // discard any coins too big to use
    val sCoins = coins.filter(_ <= money)

    // we have proper change
    if (money == 0) 1

    // no way to make proper change
    else if (sCoins.isEmpty) 0

    // for each coin small enough to use, see how many ways we can make change using it,
    // and how many ways we can make change without using it.
    // there's probably a nicer way to do it...but this is what I dreamt up
    else sCoins.map(c => countChange(money-c, sCoins.filter(_ <= c))).sum
  }
}
