package ethanp.xml2json

/**
  * 9/11/16 12:09 PM
  */
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
