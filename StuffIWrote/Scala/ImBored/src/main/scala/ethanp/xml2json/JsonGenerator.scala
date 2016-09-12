package ethanp.xml2json

/**
  * 9/11/16 8:47 PM
  */
class JsonGenerator {
    def generateJson(rootAstNode: ASTNode): JsonObj = {
        rootAstNode match {
            case elem: TextElem =>
                JsonObj(Seq(JsonField("name", elem.text)))
            case elem: DOMElem =>
                val nameField = JsonField("name", elem.name)
                val childElems: Seq[Json] = elem.textAndChildren.map {
                    case TextElem(name: String) => JsonName(name)
                    case child: DOMElem => generateJson(child)
                    case err => throw new RuntimeException(s"unexpected child elem: $err")
                }
                val childrenField = JsonField(JsonName("children"), JsonArr(childElems))
                JsonObj(nameField :: childrenField :: Nil)
        }
    }
}

object JsonGenerator {
    def generateFrom(ast: ASTNode): JsonObj =
        new JsonGenerator().generateJson(ast)
}

sealed trait Json

case class JsonObj(pairs: Seq[JsonField]) extends Json

case class JsonArr(arr: Seq[Json]) extends Json
case class JsonName(name: String) extends Json
case class JsonField(jsonName: JsonName, jsonValue: Json) extends Json
object JsonField {
    def apply(a: String, b: Any): JsonField =
        JsonField(JsonName(a), JsonValueElem(b))
}
/** could be a Double, Long, or String */
case class JsonValueElem(valueElem: Any) extends Json
