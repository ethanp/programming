package huffman

import org.scalatest.{Matchers, FlatSpec}

/**
  * Ethan Petuchowski
  * 12/30/15
  */
class CoderTest extends FlatSpec with Matchers {
    "A Coder" should "compress simple text" in {
        val (encoded, codeTree) = Coder encode "aaa"
        encoded shouldEqual "000"
        codeTree.shouldEqual(
            CodeTreeInner(4,
                CodeTreeLeaf(3, 'a'),
                CodeTreeLeaf(1, ' ')
            )
        )
        Coder.dump(encoded)
    }

    it should "also compress this" in {
        val (encoded, codeTree) = Coder encode "aaabacc"
        encoded shouldEqual "0001101010"
        codeTree.shouldEqual(
            CodeTreeInner(7,
                CodeTreeLeaf(4,'a'),
                CodeTreeInner(3,
                    CodeTreeLeaf(2, 'c'),
                    CodeTreeLeaf(1, 'b')
                )
            )
        )
        Coder.dump(encoded)
    }
}
