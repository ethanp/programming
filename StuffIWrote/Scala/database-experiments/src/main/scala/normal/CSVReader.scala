package normal

import scala.language.postfixOps

/**
  * 8/27/16 5:04 PM
  */
class CSVReader(override val path: String) extends base.CSVReader {
    override def firstLine: Line = {
        val reader = createLineStream
        reader.next() // skip header
        val firstValues = reader.next()
        parseLine(firstValues)
    }

    override def query(key: String, value: Any): Option[Line] = {
        def matchesQuery(line: String): Boolean = parseLine(line) get key contains value
        val lineOfInterestIsNext = createLineStream.dropWhile(!matchesQuery(_))
        if (lineOfInterestIsNext.isEmpty) None
        else Some(parseLine(lineOfInterestIsNext.next()))
    }

    protected val columnNames = createLineStream.next() split separator

    protected def createLineStream: Iterator[String] = io.Source.fromFile(path).getLines()

    protected def parseValues(values: Iterable[String]): Iterable[Any] = values map parseValue

    protected def parseLine(line: String): Line = {
        val values = line split this.separator
        val parsedValues = parseValues(values)
        columnNames zip parsedValues toMap
    }
}
