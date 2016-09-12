package ethanp.xml2json


import scala.collection.mutable

/**
  * 9/11/16 12:37 PM
  */
class Parser(tokens: Iterator[XMLToken]) {
    def parse(): ASTNode = {
        if (!tokens.hasNext) return DOMElem("<NO one=\"home\">")
        val nodeCreationStack = new mutable.Stack[DOMElem]()
        var previousToken: XMLToken = null
        while (tokens.hasNext) {
            val cur = tokens.next()
            cur match {
                case EOF => /* ignore */
                case LeftBracket => /* ignore */
                case RightBracket =>
                    if (previousToken == Slash) {
                        /* TODO this element has no children; we can finish now */
                    } else {
                        /* TODO this element is open until some subsequent CLOSE tag */
                    }
                case Equals => /* ignore */
                case Slash =>
                    if (previousToken == LeftBracket) {
                        /* TODO this is a CLOSE tag */
                    } else {
                        /* ignore (handled by `RightBracket`) */
                    }
                case Name(name) =>
                    if (previousToken == LeftBracket) {
                        /* TODO this is an OPEN tag, so initialize a new DOM node */
                        nodeCreationStack push DOMElem(name)
                    } else if (previousToken == Slash) {
                        /* TODO its a CLOSE tag, so I guess we can pop off the stack */
                        /* TODO verify that it is the right name (i.e. type-check it?) */
                    } else {
                        /* TODO its the name of a `Value` which is coming next */
                    }
                case Value(value) =>
                    /* TODO add it with a name */
            }
            previousToken = cur
        }
        DOMElem("asdf")
    }
}

object Parser {
    def parse(tokens: Iterable[XMLToken]): ASTNode =
        new Parser(tokens.iterator).parse()
}

sealed trait ASTNode

case class TextElem(text: String) extends ASTNode

case class DOMElem(
    // e.g. for <p></p> or <p/> it would be "p"
    name: String,
    // attribute name-value pairs (TODO anything else?)
    simpleFields: mutable.Map[String, Any] = mutable.Map.empty,
    // interspersed text sequences and child nodes
    textAndChildren: mutable.Seq[ASTNode] = mutable.Seq.empty
) extends ASTNode
