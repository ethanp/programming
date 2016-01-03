package artificialIntelligence.naturalLanguageProcessing.deterministicSegmentation

import scala.language.postfixOps

/**
  * Ethan Petuchowski
  * 1/3/16
  *
  * Turn e.g.
  *
  *     www.checkdomain.com => [check domain]
  *     #honestyhour => [honesty hour]
  *     #30secondstoearth => [30 seconds to earth]
  */
object Solution {

    val number = """\d+(\.\d+)?""".r
    val dub = """^(?i)www\.""".r  // note: I think (?i) will make it case-insensitive
    val domain = """(?i)\.(com|edu|org|gov|in|co\.uk|ru|io|com\.cn|cz|tk)$""".r
    def isHash(str: String) = str.head == '#'
    val words = io.Source.fromFile("words.txt").getLines().toSeq.sortWith(_.length > _.length)
    val word = words.mkString("(?i)(", "|", ")").r


    def main(args: Array[String]): Unit = {
        val input = trial()
        val N = input.next().toInt
        for (_ ← 1 to N) {
            val str = input.next()
            val a = word.findAllIn(str)
            println(a mkString " ")
        }
    }

    def real(): Iterator[String] = io.Source.stdin.getLines()

    def trial(): Iterator[String] = List("9",
        "economist.com",
        "marketwatch.com",
        "me.com",
        "liveinternet.ru",
        "howstuffworks.com",
        "timesonline.co.uk",
        "example.com",
        "patch.com",
        "sun.com"
    ) toIterator
}
