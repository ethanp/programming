package ethanp.xml2json

/**
  * 9/11/16 12:55 AM
  */
object GetIt extends App {
    import ethanp.xml2json.Tokens.Token
    val t = new Tokenizer(Test.testString.iterator)
    val tokens: Seq[Token] = t.extractAllTokens()
    tokens foreach println
}
