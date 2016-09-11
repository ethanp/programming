package ethanp.xml2json

/**
  * 9/11/16 12:55 AM
  */
object GetIt extends App {
    class Tokenizer(inputStream: Iterator[Char]) {
        sealed trait Token
        case object EOF extends Token
        case object LeftBracket extends Token
        case object RightBracket extends Token
        case object Equals extends Token
        case object Slash extends Token
        case class Name(name: String) extends Token
        case class Value(value: String) extends Token

        val whitespaceChars = Set(' ', ' ', '\t', '\n')
        def hasNextToken = inputStream.hasNext
        def nextToken(): Token = {
            if (!inputStream.hasNext) return EOF
            val isAlphaNum = (c: Char) => Character isLetterOrDigit c
            val isWhitespace = (c: Char) => whitespaceChars contains c
            inputStream.next() match {
                case x if isWhitespace(x) => nextToken()
                case '<' => LeftBracket
                case '>' => RightBracket
                case '=' => Equals
                case '/' => Slash
                case firstLetter if isAlphaNum(firstLetter) =>
                    val word = inputStream takeWhile isAlphaNum
                    Name(firstLetter +: word.mkString)
                case '"' =>
                    val word = inputStream takeWhile isAlphaNum
                    inputStream.next() // skip closing quote
                    Value(word.mkString)
                case x =>
                    println(s"unrecognized: $x")
                    EOF
            }
        }
    }
    val t = new Tokenizer(Test.testString.iterator)
    while (t.hasNextToken) {
        println(t.nextToken())
    }
}
object Test {
    // Mak, Ronald. Writing Compilers and Interpreters: A Software Engineering
    // Approach (Kindle Location 6639). Wiley. Kindle Edition.
    val testString =
        """<COMPOUND line="18">
          |    <ASSIGN line="19">
          |        <VARIABLE id="alpha" level="0" />
          |        <NEGATE>
          |            <INTEGER_CONSTANT value="88" />
          |        </NEGATE>
          |    </ASSIGN>
          |    <ASSIGN line="20">
          |        <VARIABLE id="beta" level="0" />
          |        <INTEGER_CONSTANT value="99" />
          |    </ASSIGN>
          |    <ASSIGN line="21">
          |        <VARIABLE id="result" level="0" />
          |        <ADD>
          |            <ADD>
          |                <VARIABLE id="alpha" level="0" />
          |                <FLOAT_DIVIDE>
          |                    <INTEGER_CONSTANT value="3" />
          |                    <SUBTRACT>
          |                        <VARIABLE id="beta" level="0" />
          |                        <VARIABLE id="gamma" level="0" />
          |                    </SUBTRACT>
          |                </FLOAT_DIVIDE>
          |            </ADD>
          |            <INTEGER_CONSTANT value="5" />
          |        </ADD>
          |    </ASSIGN>
          |</COMPOUND>""".stripMargin
}
