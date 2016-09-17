package ethanp.xml2json

import scala.collection.mutable

/**
  * 9/11/16 10:32 PM
  */
object JsonPrinter {

    def compactPrint(arr: JsonArr): String = "[" + (arr.arr map compactPrintJson mkString ",") + "]"

    def compactPrint(json: JsonObj): String = {
        val fieldStrings: Seq[String] = json.pairs.map { field =>
            "\"%s\":%s".format(field.jsonName.name, compactPrintJson(field.jsonValue))
        }
        "{" + (fieldStrings mkString ",") + "}"
    }

    def prettyPrint(json: JsonObj): String = {
        val INDENT_WIDTH = 4
        val compact: Iterator[Char] = compactPrint(json).iterator
        val result = new mutable.StringBuilder
        var currIndent = 0
        var inAString = false
        def indent: String = " " * (INDENT_WIDTH * currIndent)
        def downOneIndent(): Unit = {
            result.setLength(result.length - INDENT_WIDTH)
            currIndent -= 1
        }
        while (compact.hasNext) {
            val next = compact.next()
            next match {
                case '{' | '[' =>
                    result append next + "\n"
                    currIndent += 1
                    result append indent
                case '}' | ']' =>
                    result append "\n" + indent
                    downOneIndent()
                    result append next
                case '"' =>
                    // quote-escaping is not implemented
                    inAString = !inAString
                    result append next
                case ',' if !inAString => result append ",\n" + indent
                case ':' if !inAString => result append " : "
                case ch => result append ch
            }
        }
        result.toString()
    }

    private def compactPrintJson(json: Json): String = json match {
        case obj: JsonObj => compactPrint(obj)
        case arr: JsonArr => compactPrint(arr)
        case JsonName(name) => "\"%s\"".format(name)
        case JsonField(jsonName, jsonValue) =>
            throw new RuntimeException("shouldn't have a field here")
        case JsonValue(value) => value match {
            case s: String => "\"%s\"".format(s)
            case x => x.toString
        }
    }
}
