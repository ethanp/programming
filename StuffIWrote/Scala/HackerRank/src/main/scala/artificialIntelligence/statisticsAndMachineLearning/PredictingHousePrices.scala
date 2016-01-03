package artificialIntelligence.statisticsAndMachineLearning

import scala.language.postfixOps

/**
  * Ethan Petuchowski
  * 1/2/16
  */
object Solution {
    def main(args: Array[String]): Unit = {

        /* parse all the things */
        val lines = io.Source.stdin.getLines() toSeq
        val first = lines.head split " " map (_ toInt) toSeq
        val f :: n :: _ = first
        def parseVals(ss: String) = ss split " " map (_ toDouble)
        val trainData = lines.tail take n map parseVals
        val afterData = lines.tail drop n
        val T = afterData.head.toInt
        val testData = afterData.tail map parseVals

        val Y: Seq[Double] = trainData map (_ last)
        val X = trainData map (1.0 +: _.init)
        val Xt = X.transpose

        /* I _think_ this is right...but really I have no idea. */
        val XtX: Seq[Seq[Double]] =
            for (row ← X) yield
                for (col ← X) yield
                    (row zip col foldLeft 0.0) {
                        case (t, (a, b)) => t + a * b }

        /* except now I'd have to implement inverse
         * and that's looking a bit scary */
    }
}
