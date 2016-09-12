package ethanp.xml2json

import scala.collection.mutable

/**
  * 9/11/16 10:32 PM
  */
object JsonPrinter {
    def print(json: JsonObj): Seq[String] = {
        def arrInnerString(arr: JsonArr): Seq[String] = {
            arr.arr.flatMap { (arrayElem: Json) =>
                /* there's a separate format for when the object is part of an array
                           rather than a field */
                arrayElem match {
                    case j: JsonObj => print(j)
                    case a: JsonArr => arrInnerString(a)
                    case JsonName(name) => Seq("")
                    case JsonField(jsonName, jsonValue) => Seq("")
                    case JsonValueElem(valueElem) => Seq("")
                }
            }
        }
        val seq: Seq[String] = json.pairs.flatMap { x: JsonField =>
            // TODO I NEED TO SWITCH THE APPROACH INSTEAD OF DOING THIS
            // there's probably a simpler (though more object-y) way to do it instead
            // of building up the string directly which is leaving me with spaghetti
            val lines = mutable.ArrayBuffer.empty[String]
            x.jsonValue match {
                case y: JsonObj =>
                    lines += x.jsonName.name + ": {"
                    lines ++= print(y)
                case arr: JsonArr =>
                    lines += x.jsonName.name + ": ["
                    lines ++= arrInnerString(arr)

                case JsonName(name) =>
                case JsonField(jsonName, jsonValue) =>
                case JsonValueElem(valueElem) =>
            }
            lines
        }
        ???
    }
}
