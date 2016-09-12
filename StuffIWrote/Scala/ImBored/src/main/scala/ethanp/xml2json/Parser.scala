package ethanp.xml2json


import scala.collection.mutable

/**
  * 9/11/16 12:37 PM
  */
class Parser(tokens: Iterator[XMLToken]) {
    def parse(): ASTNode = {
        if (!tokens.hasNext) return DOMElem("<NO one=\"home\">")
        val nodeCreationStack = new mutable.Stack[DOMElem]()
        var rootNode: DOMElem = null
        var previousToken: XMLToken = null
        /**
          * @param expectedName when this is passed in, a warning is issued if it doesn't match
          *                     the name of the node being popped off the stack
          */
        def popNodeOff(expectedName: Option[String] = None): Unit = {
            /* its a CLOSE tag, so we just pop off the stack */
            val node = nodeCreationStack.pop()
            /* warn if the name is wrong (but move on) */
            if (expectedName.nonEmpty && expectedName.get != node.name) {
                System.err.println(s"WARNING: invalid close-tag ${node.name}, expected ${expectedName.get}; continuing")
            }
        }
        tokens foreach { cur =>
            println(cur)
            cur match {
                case Name(name: String) =>
                    if (previousToken == LeftBracket) {
                        /* this is an OPEN tag, so initialize a new DOM node */
                        val newDomElem = DOMElem(name)
                        if (rootNode == null) {
                            rootNode = newDomElem
                        } else {
                            nodeCreationStack.head.addChild(newDomElem)
                        }
                        nodeCreationStack.push(newDomElem)
                    } else if (previousToken == Slash) {
                        popNodeOff(expectedName = Some(name))
                    } else {
                        /* ignore (its the name of a `Value` which is coming next) */
                    }
                case RightBracket =>
                    if (previousToken == Slash) {
                        /* this element has no children; we can finish now */
                        popNodeOff()
                    } else {
                        /* ignore (this element is open until some subsequent CLOSE tag) */
                    }
                case Slash =>
                    if (previousToken == LeftBracket) {
                        /* ignore (this is a CLOSE tag, but it's handled in the Name above) */
                    } else {
                        /* ignore (handled by `RightBracket`) */
                    }
                case Value(value: String) =>
                    /* add it with a name */
                    def tryDouble: Any = {
                        try value.toDouble
                        catch {case e: NumberFormatException => value}
                    }
                    def tryParseLongOrDouble: Any = {
                        try value.toLong
                        catch {case e: NumberFormatException => tryDouble}
                    }
                    val parsedValue = tryParseLongOrDouble
                    val associatedName = previousToken.asInstanceOf[Name].name
                    nodeCreationStack.head.simpleFields += (associatedName -> parsedValue)
                case EOF | LeftBracket | Equals => /* ignore (nothing to do) */
            }
            if (cur != Equals) {
                previousToken = cur
            }
        }
        rootNode
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
    textAndChildren: mutable.ArrayBuffer[ASTNode] = mutable.ArrayBuffer.empty
) extends ASTNode {
    def addChild(child: DOMElem) = textAndChildren append child
}
