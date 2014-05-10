import java.io.{File, PrintWriter}
import scala.io.Source

/**
 * Ethan Petuchowski
 * 5/9/14
 */


object DataIO {
  lazy val readCSV: Array[String] = {
    val dirName = "/Users/Ethan/Downloads/afm 2"
    val afmFiles: Array[File] =
      new java.io.File(dirName)
        .listFiles
        .filter(_.getName.endsWith(".afm"))

    // ["font_id,letter,letter,value",...]
    afmFiles.zipWithIndex map { case (file, i) =>
      Source.fromFile(file).getLines()
        .filter(_ startsWith "KPX")
        .map(i + _.replaceFirst("KPX", "").replaceAll(" ", ","))
        .toList
    } flatten
  }

  def writeModel(filename: String, outCSV: String) {
     val modelDataWriter = new PrintWriter(filename)
    modelDataWriter write outCSV
    modelDataWriter.close()
  }

    // reduce to ("letter,letter" -> PairInfo)
  lazy val pairCounts: Map[String, PairInfo] =
    readCSV.groupBy(i => FontLine(i).pairString)
      .toList.zipWithIndex.map { case (k, v) =>
        k._1 -> PairInfo(v, k._2.size)
      } toMap

  // CSV: (font, pair, kernValue)
  def csvOfCountAbove(lowerThreshold: Int): String =
    readCSV.flatMap { line =>
      val fontLine = FontLine(line)
      val pair = fontLine.pairString
      if (pairCounts(pair).count < lowerThreshold) None
      else Some {
        List(fontLine.fontId, pairCounts(pair).id, fontLine.kernValue).mkString(",")
      }
    } mkString "\n"
}

// PairInfo : gives a unique id and a seen-count to each letter pair
case class PairInfo(id: Int, var count: Int) { // case classiness allows for nice syntax later
    def asStringArray = Array(id.toString, count.toString)
}

case class FontLine(line: String) {
  val split = line.split(",")
  val fontId = split(0)
  val letter1 = split(1)
  val letter2 = split(2)
  val kernValue = split(3)
  def pairString = s"$letter1,$letter2"
}


/**
 * Process the raw files into something suitable for mahout
 *
 * Also output a CSV of [Letter1, Letter2, Count, PairID] data, sorted by Count descending
 */
object AllData extends App {
  DataIO.writeModel("AllTheData.csv", DataIO.csvOfCountAbove(0))
}


/**
 * Only use pairs that belong to at least 10 fonts, there are 158 such pairs
 */
object PopularityContest extends App {
  DataIO.writeModel("10AndUpData.csv", DataIO.csvOfCountAbove(10))
}

object DataHistogram extends App {
  /** histogram : sorted csv histogram of letter pairs

     "highest,pair,count,id
      nextHighest,pair,count,id
      ... "
    */
  val histogramCSV: String = DataIO.pairCounts
    .map { case (pair, info) => (pair split ",") ++ info.asStringArray }.toList
    .sortWith( _(2).toInt > _(2).toInt )  // sort by count, descending
    .map(_ mkString ",") mkString "\n"    // turn to CSV string

  val header = "FirstLetter,SecondLetter,ID,Count\n"
  val histogramWriter = new PrintWriter("KerningHistogram.csv")
  histogramWriter write header + histogramCSV     // write to file
  histogramWriter.close()
}
