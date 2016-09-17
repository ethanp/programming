package ethanp.xml2json

/**
  * 9/11/16 10:32 PM
  */
object JsonPrinter {

    def compactPrint(arr: JsonArr): String = {
        "[" + (arr.arr map printJson mkString ",") + "]"
    }

    def printJson(json: Json): String = json match {
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

    def compactPrint(json: JsonObj): String = {
        val fieldStrings: Seq[String] = json.pairs.map { field =>
            "\"%s\":%s".format(field.jsonName.name, printJson(field.jsonValue))
        }
        "{" + (fieldStrings mkString ",") + "}"
    }

    def prettyPrint(json: JsonObj): String = {
        // TODO finish beautifying it
        val compact: Iterator[Char] = compactPrint(json).iterator
        while (compact.hasNext) {
            val next = compact.next()
            next match {
                case '{' => println("hello world")
            }
        }
    }
}
