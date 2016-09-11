package ethanp.xml2json

import scala.collection.mutable

/**
  * 9/11/16 12:08 PM
  */
private class Tokenizer(inputStream: Iterator[Char]) {

    def hasNextToken = curChar.nonEmpty

    def nextToken(): XMLToken = {
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

    def extractAllTokens(): Seq[XMLToken] = {
        val arr = mutable.ArrayBuffer.empty[XMLToken]
        while (hasNextToken) arr += nextToken()
        arr.toVector
    }

    private def isWordChar(char: Char) = char.isLetterOrDigit || char == '_'

    private var curChar: Option[Char] = moveToNextChar()

    private def advance(): Unit = {curChar = moveToNextChar()}

    private def moveToNextChar(): Option[Char] =
        if (inputStream.hasNext)
            Some(inputStream.next())
        else None
}

object Tokenizer {
    def tokenize(iterator: Iterator[Char]): Seq[XMLToken] =
        new Tokenizer(iterator).extractAllTokens()
}

sealed trait XMLToken
case object EOF extends XMLToken
case object LeftBracket extends XMLToken
case object RightBracket extends XMLToken
case object Equals extends XMLToken
case object Slash extends XMLToken
case class Name(name: String) extends XMLToken
case class Value(value: String) extends XMLToken
