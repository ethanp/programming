package normal

import scala.language.postfixOps

/**
  * 8/27/16 5:04 PM
  */
class CSVReader(override val path: String) extends base.CSVReader {
    val headerKeys = createLineStream.next() split separator

    def parseLine(line: String): Line = {
        val values = line split this.separator
        val parsedValues = parseValues(values)
        headerKeys zip parsedValues toMap
    }

    def parseValues(values: Array[String]): Array[Any] = values map parseValue

    override def firstLine: Line = {
        val reader = createLineStream
        reader.next() // skip header
        val firstValues = reader.next()
        parseLine(firstValues)
    }

    def createLineStream: Iterator[String] = io.Source.fromFile(path).getLines()

    override def query(key: String, value: Any): Option[Line] = {
        val lineOfInterestIsNext = createLineStream.dropWhile { s =>
            val parsed = parseLine(s)
            !(
                (parsed contains key)
                    && (parsed(key) == value))
        }
        if (lineOfInterestIsNext.isEmpty) None
        else Some {
            val line = lineOfInterestIsNext.next()
            parseLine(line)
        }
    }
}
