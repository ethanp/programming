package artificialIntelligence.naturalLanguageProcessing.sentenceSplitting

import java.io.StringReader
import java.util

import edu.stanford.nlp.ling.HasWord
import edu.stanford.nlp.process.DocumentPreprocessor
import scala.collection.JavaConverters._

/**
* Ethan Petuchowski
* 1/3/16
*/
object Solution {
    def main(args: Array[String]): Unit = stanfordVrsn()

    /** This didn't work nearly as well as I expected...
      * There is some cleaning left to be done here.
      */
    def stanfordVrsn(): Unit = {
        val input = io.Source.stdin.getLines().next()
        val preProc = new DocumentPreprocessor(new StringReader(input)).iterator()
        while (preProc.hasNext) {
            val next: util.List[HasWord] = preProc.next()
            val string = next.asScala.mkString(" ")
            val all = string
                .replaceAll(" ''", "\"")
                .replaceAll("`` ", "\"")
                .replaceAll(" \\.", "\\.")
                .replaceAll(" \\?", "\\? ")
                .replaceAll(" \\!", "\\!")
                .replaceAll(" '", "'")
                .replaceAll(" ,", ",")
            println(all)
        }
    }
}
