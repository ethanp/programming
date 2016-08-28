package base

/**
  * 8/27/16 5:07 PM
  */
trait CSVReader {
    type Line = Map[String, Any]
    def path: String
    def separator = ","
    def firstLine: Line
    def query(key: String, value: Any): Option[Line]
    def parseValue(v: String): Any = {
        try v.toLong
        catch {
            case e: NumberFormatException => try v.toDouble
            catch {case e: NumberFormatException => v}
        }
    }
}
