package ethanp.xml2json

/**
  * 9/11/16 12:08 PM
  */
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
