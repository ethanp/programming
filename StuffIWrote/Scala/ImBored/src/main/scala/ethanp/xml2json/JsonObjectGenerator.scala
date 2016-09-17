package ethanp.xml2json

/**
  * 9/11/16 8:47 PM
  */
object JsonObjectGenerator {
    def fromAST(rootAstNode: ASTNode): JsonObj = {
        rootAstNode match {
            case elem: TextElem =>
                JsonObj(Seq(JsonField(JsonName("name"), JsonValue(elem.text))))
            case elem: DOMElem =>
                val nameField = JsonField(JsonName("name"), JsonValue(elem.name))
                val childElems: Seq[Json] = elem.textAndChildren.map {
                    case TextElem(name: String) => JsonName(name)
                    case child: DOMElem => JsonObjectGenerator.fromAST(child)
                    case err => throw new RuntimeException(s"unexpected child elem: $err")
                }
                val childrenField = JsonField(JsonName("children"), JsonArr(childElems))
                JsonObj(nameField :: childrenField :: Nil)
        }
    }
}

sealed trait Json
case class JsonObj(pairs: Seq[JsonField]) extends Json
case class JsonArr(arr: Seq[Json]) extends Json
case class JsonName(name: String) extends Json
case class JsonField(jsonName: JsonName, jsonValue: Json) extends Json
/** could be a Double, Long, String (...I think) */
case class JsonValue(value: Any) extends Json
