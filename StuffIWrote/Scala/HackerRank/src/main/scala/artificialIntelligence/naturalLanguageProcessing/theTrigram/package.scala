package artificialIntelligence.naturalLanguageProcessing

import java.util

import scala.collection.mutable

/**
  * Ethan Petuchowski
  * 1/3/16
  */
object Solution {
    def main(args: Array[String]): Unit = {
        val counter = mutable.Map[String, Int]().withDefaultValue(0)
        val dequeue = new util.ArrayDeque[String]()

        // correct is "asdf qwer zxcv"
        val input = real()
        val words: Array[String] = input
            .toLowerCase
            .replaceAll(","," ,")
            .replaceAll("\\."," .")
            .replaceAll("!"," !")
            .replaceAll("\\?"," ?")
            .replaceAll("[^\\p{L}€\u009D,.!? ]", " ")
            .replaceAll("\\s+", " ")
            .split(" ")

        for (word ← words) {
            dequeue add word
            if (dequeue.size() > 3) dequeue.removeFirst()
            val hashable = dequeue.toArray(new Array[String](3)).mkString(" ")
            if (punctuation.findAllIn(hashable).isEmpty)counter(hashable) += 1
        }

        counter.toSeq.sortWith(_._2 > _._2) take 10 foreach println
        println(counter.maxBy(_._2)._1)
    }
    val punctuation = "[.?!,]".r
    def test = "Asdf qwer zxcv asdf fghj asdf\nqwer zxcv."
    def real() = {
        import java.nio.charset.{Charset, CodingErrorAction}
        val decoder = Charset.forName("UTF-8").newDecoder()
        decoder.onMalformedInput(CodingErrorAction.IGNORE)
        val source = scala.io.Source.fromInputStream(System.in)(decoder)
        source.getLines().mkString(" ")
    }
}
