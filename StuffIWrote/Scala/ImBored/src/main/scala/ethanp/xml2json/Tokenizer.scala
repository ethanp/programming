package ethanp.xml2json

import scala.collection.mutable

/**
  * 9/11/16 12:08 PM
  */
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
