package ethanp.xml2json

/**
  * 9/11/16 12:55 AM
  */
object Main extends App {
    private val egItr = Test.exampleXML.iterator
    val tokens: Seq[XMLToken] = Tokenizer tokenize egItr
    assert(tokens.mkString("\n") == Test.expectedTokenization, "tokenization")
}
