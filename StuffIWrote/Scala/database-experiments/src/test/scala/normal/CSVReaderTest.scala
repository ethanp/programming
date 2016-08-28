package normal

import org.scalatest.{FlatSpec, Matchers}

/**
  * 8/27/16 5:05 PM
  */
class CSVReaderTest extends FlatSpec with Matchers {
    val firstLine = Map[String, Any](
        "file_id" -> 203,
        "time" -> 12,
        "cell_id" -> 1,
        "d1" -> 25344,
        "d2" -> 27968,
        "fsc_small" -> 34677,
        "fsc_perp" -> 14944,
        "fsc_big" -> 32400,
        "pe" -> 2216,
        "chl_small" -> 28237,
        "chl_big" -> 5072,
        "pop" -> "pico"
    )

    def makeReader: CSVReader = new CSVReader("/Users/Ethan/code/Coursera/datasci_course_materials/assignment5/seaflow_21min.csv")

    "a CSVReader" should "get me a line" in {
        val reader = makeReader
        reader.firstLine shouldEqual firstLine
    }

    it should "find me a line" in {
        val reader = makeReader
        val result = reader.query("file_id", 203)
        result should not be empty
        result.get shouldEqual firstLine
    }

    it should "not see what is not there" in {
        val reader = makeReader
        val result = reader.query("file_id", 23)
        result should be(empty)
    }
}
