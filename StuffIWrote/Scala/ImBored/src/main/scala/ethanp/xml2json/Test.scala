package ethanp.xml2json

/**
  * 9/11/16 12:09 PM
  */
object Test {
    // Mak, Ronald. Writing Compilers and Interpreters: A Software Engineering
    // Approach (Kindle Location 6639). Wiley. Kindle Edition.
    val exampleXML =
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

    val expectedTokenization =
        """LeftBracket
          |Name(COMPOUND)
          |Name(line)
          |Equals
          |Value(18)
          |RightBracket
          |LeftBracket
          |Name(ASSIGN)
          |Name(line)
          |Equals
          |Value(19)
          |RightBracket
          |LeftBracket
          |Name(VARIABLE)
          |Name(id)
          |Equals
          |Value(alpha)
          |Name(level)
          |Equals
          |Value(0)
          |Slash
          |RightBracket
          |LeftBracket
          |Name(NEGATE)
          |RightBracket
          |LeftBracket
          |Name(INTEGER_CONSTANT)
          |Name(value)
          |Equals
          |Value(88)
          |Slash
          |RightBracket
          |LeftBracket
          |Slash
          |Name(NEGATE)
          |RightBracket
          |LeftBracket
          |Slash
          |Name(ASSIGN)
          |RightBracket
          |LeftBracket
          |Name(ASSIGN)
          |Name(line)
          |Equals
          |Value(20)
          |RightBracket
          |LeftBracket
          |Name(VARIABLE)
          |Name(id)
          |Equals
          |Value(beta)
          |Name(level)
          |Equals
          |Value(0)
          |Slash
          |RightBracket
          |LeftBracket
          |Name(INTEGER_CONSTANT)
          |Name(value)
          |Equals
          |Value(99)
          |Slash
          |RightBracket
          |LeftBracket
          |Slash
          |Name(ASSIGN)
          |RightBracket
          |LeftBracket
          |Name(ASSIGN)
          |Name(line)
          |Equals
          |Value(21)
          |RightBracket
          |LeftBracket
          |Name(VARIABLE)
          |Name(id)
          |Equals
          |Value(result)
          |Name(level)
          |Equals
          |Value(0)
          |Slash
          |RightBracket
          |LeftBracket
          |Name(ADD)
          |RightBracket
          |LeftBracket
          |Name(ADD)
          |RightBracket
          |LeftBracket
          |Name(VARIABLE)
          |Name(id)
          |Equals
          |Value(alpha)
          |Name(level)
          |Equals
          |Value(0)
          |Slash
          |RightBracket
          |LeftBracket
          |Name(FLOAT_DIVIDE)
          |RightBracket
          |LeftBracket
          |Name(INTEGER_CONSTANT)
          |Name(value)
          |Equals
          |Value(3)
          |Slash
          |RightBracket
          |LeftBracket
          |Name(SUBTRACT)
          |RightBracket
          |LeftBracket
          |Name(VARIABLE)
          |Name(id)
          |Equals
          |Value(beta)
          |Name(level)
          |Equals
          |Value(0)
          |Slash
          |RightBracket
          |LeftBracket
          |Name(VARIABLE)
          |Name(id)
          |Equals
          |Value(gamma)
          |Name(level)
          |Equals
          |Value(0)
          |Slash
          |RightBracket
          |LeftBracket
          |Slash
          |Name(SUBTRACT)
          |RightBracket
          |LeftBracket
          |Slash
          |Name(FLOAT_DIVIDE)
          |RightBracket
          |LeftBracket
          |Slash
          |Name(ADD)
          |RightBracket
          |LeftBracket
          |Name(INTEGER_CONSTANT)
          |Name(value)
          |Equals
          |Value(5)
          |Slash
          |RightBracket
          |LeftBracket
          |Slash
          |Name(ADD)
          |RightBracket
          |LeftBracket
          |Slash
          |Name(ASSIGN)
          |RightBracket
          |LeftBracket
          |Slash
          |Name(COMPOUND)
          |RightBracket""".stripMargin

    val expectedResult =
        """{
          |    "type": "COMPOUND",
          |    "line": 18,
          |    "children": [
          |        {
          |            "type": "ASSIGN",
          |            "line": 19,
          |            "children": [...]
          |        }, { ... }
          |    ]
          |}
        """.stripMargin
}
