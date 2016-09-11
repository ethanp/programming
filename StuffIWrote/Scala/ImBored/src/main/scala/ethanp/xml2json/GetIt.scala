package ethanp.xml2json

import scala.collection.mutable

/**
  * 9/11/16 12:55 AM
  */
object GetIt extends App {

    class Tokenizer(inputStream: Iterator[Char]) {

        import ethanp.xml2json.Tokens._

        def hasNextToken = curChar.nonEmpty

        def isWordChar(char: Char) = char.isLetterOrDigit || char == '_'

        var curChar: Option[Char] = moveToNextChar()

        def advance(): Unit = {curChar = moveToNextChar()}

        def moveToNextChar(): Option[Char] =
            if (inputStream.hasNext)
                Some(inputStream.next())
            else None

        def nextToken(): Token = {
            if (curChar.isEmpty) return EOF
            val ret = curChar.get match {
                case x if x.isWhitespace =>
                    advance()
                    return nextToken()
                case '<' => LeftBracket
                case '>' => RightBracket
                case '=' => Equals
                case '/' => Slash
                case c if isWordChar(c) =>
                    val buf = new mutable.StringBuilder()
                    while (curChar exists isWordChar) {
                        buf append curChar.get
                        advance()
                    }
                    return Name(buf.toString)
                case '"' =>
                    val word = inputStream takeWhile isWordChar
                    // skips '"' (close-quote) automatically
                    Value(word.mkString)
                case x =>
                    System.err.println(s"unrecognized character: $x")
                    EOF
            }
            advance()
            ret
        }

        def extractAllTokens(): Seq[Token] = {
            val arr = mutable.ArrayBuffer.empty[Token]
            while (hasNextToken) arr += nextToken()
            arr.toVector
        }
    }

    import ethanp.xml2json.Tokens.Token

    val t = new Tokenizer(Test.testString.iterator)
    val tokens: Seq[Token] = t.extractAllTokens()
    tokens foreach println
}

object Tokens {
    sealed trait Token
    case object EOF extends Token
    case object LeftBracket extends Token
    case object RightBracket extends Token
    case object Equals extends Token
    case object Slash extends Token
    case class Name(name: String) extends Token
    case class Value(value: String) extends Token
}

object Test {
    // Mak, Ronald. Writing Compilers and Interpreters: A Software Engineering
    // Approach (Kindle Location 6639). Wiley. Kindle Edition.
    val testString =
    """<COMPOUND line="18">
      |   <ASSIGN line="19">
      |       <VARIABLE id="alpha" level="0" />
      |       <NEGATE>
      |           <INTEGER_CONSTANT value="88" />
      |       </NEGATE>
      |   </ASSIGN>
      |   <ASSIGN line="20">
      |       <VARIABLE id="beta" level="0" />
      |       <INTEGER_CONSTANT value="99" />
      |   </ASSIGN>
      |   <ASSIGN line="21">
      |       <VARIABLE id="result" level="0" />
      |       <ADD>
      |           <ADD>
      |               <VARIABLE id="alpha" level="0" />
      |               <FLOAT_DIVIDE>
      |                   <INTEGER_CONSTANT value="3" />
      |                   <SUBTRACT>
      |                       <VARIABLE id="beta" level="0" />
      |                       <VARIABLE id="gamma" level="0" />
      |                   </SUBTRACT>
      |               </FLOAT_DIVIDE>
      |           </ADD>
      |           <INTEGER_CONSTANT value="5" />
      |       </ADD>
      |   </ASSIGN>
      |</COMPOUND>""".stripMargin
}
