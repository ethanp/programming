package ethanp.xml2json

import scala.collection.mutable

/**
  * 9/11/16 8:47 PM
  */
class JsonGenerator {
    def generateJson(rootAstNode: ASTNode): JsonObj = {
        rootAstNode match {
            case elem: TextElem => return basicObjWithName(elem.text)
            case elem: DOMElem =>
                JsonObj(Seq(
                    (JsonName("name"), JsonValue(JsonValueElem(elem.name)))
                ) ++ elem.textAndChildren.map {
                    // TODO this part is not quite correct yet
                    case TextElem(name: String) =>
                        (JsonName(name), JsonValue(JsonValueElem(name)))
                    case x: DOMElem =>
                        (JsonName("asdf"), JsonValue(JsonValueElem("asdf")))
                    case x => throw new RuntimeException(
                        s"unexpected thingy: $x"
                    )
                })
        }
        JsonObj()
    }

    def basicObjWithName(name: String) = JsonObj(Seq((
        JsonName("name"),
        JsonValue(JsonValueElem(name)))
    ))
}

object JsonGenerator {
    def generateFrom(ast: ASTNode): JsonObj =
        new JsonGenerator().generateJson(ast)
}

sealed trait Json

case class JsonObj(
    // TODO it may not need to be mutable
    pairs: Seq[(JsonName, JsonValue)] = mutable.ArrayBuffer.empty
) extends Json

case class JsonArr(arr: Seq[JsonValue]) extends Json
case class JsonName(name: String) extends Json
case class JsonValue(json: Json) extends Json

/** could be a Double, Long, or String */
case class JsonValueElem(valueElem: Any) extends Json
