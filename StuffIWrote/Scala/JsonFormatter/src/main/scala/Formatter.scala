import java.io.PrintStream

/** Ethan Petuchowski
  * 2/19/16
  */
object Formatter {

    var idx = 0
    var input = """{"a":"b"}"""
    val spaces = " \t\r\n".toSet
    var indent = 0
    val digits = ('0' to '9').toSet
    val numberPrefixes = "+-".toSet
    val out: PrintStream = System.out

    def curChar = input charAt idx

    def curIndent = " " * (2 * indent)

    def advance(): Char = {
        val was = curChar
        idx += 1
        if (idx == input.length)
            throw new RuntimeException("unexpected end of input")
        was
    }

    def main(args: Array[String]): Unit = {
        for (i ← Seq(
            """{"\"fda": 234, "fa" : 432, "\"abcd\ " : [23]}""",
            """[{"a":"b", "c" : [234E43, "3ed"]}, {"d": {"e": -33}}]"""
        )) {
            idx = 0
            indent = 0
            input = i
            parse()
        }
    }


    def parse(): Unit = {
        skipSpaces()
        if (!(matchObj(isValue = false) || matchArr(isValue = false)))
            throw new RuntimeException(
                s"expected { or [ but found: $curChar")
        out.println()
        skipSpaces()
        if (idx != input.length)
            throw new RuntimeException(
                s"expected end of input but found: $curChar")
    }

    def matchObj(isValue: Boolean): Boolean = {
        if (curChar == '{') {
            if (isValue) out.println(advance())
            else out.println(curIndent + advance())
            indent += 1
            readInsideObject()
            true
        }
        else false
    }

    def matchArr(isValue: Boolean): Boolean = {
        if (curChar == '[') {
            if (isValue) out.println(advance())
            else out.println(curIndent + advance())
            indent += 1
            readInsideArray()
            true
        }
        else false
    }

    def readInsideObject(): Unit = {
        skipSpaces()
        out.print(curIndent + readString())
        skipSpaces()
        if (curChar != ':')
            throw new RuntimeException(
                s"expected : but found: $curChar")
        out.print(advance()+" ")
        skipSpaces()
        readValue(inArray = false)
        skipSpaces()
        curChar match {
            case ',' ⇒
                out.println(advance())
                readInsideObject()
            case '}' ⇒
                out.println()
                indent -= 1
                out.print(curIndent + curChar)
                idx += 1
            case _ ⇒
                throw new RuntimeException(
                    s"expected , or } but found: $curChar")
        }
    }

    def readValue(inArray: Boolean): Unit = {
        curChar match {
            case '{' ⇒ matchObj(isValue = !inArray)
            case '[' ⇒ matchArr(isValue = !inArray)
            case ch if (digits | numberPrefixes) contains ch ⇒
                if (inArray) out.print(curIndent)
                out.print(readNumber())
            case '"' ⇒
                if (inArray) out.print(curIndent)
                out.print(readString())
            case _ ⇒
                throw new RuntimeException(
                    s"""expected {, [, digit, +, -, or " but found $curChar""")
        }
    }

    def readNumber(): String = {
        val sb = new StringBuilder
        sb.append(advance())
        val end = "}],".toSet | spaces
        val ees = "Ee".toSet
        var foundEes = false
        var foundDot = false
        while (idx < input.length) {
            curChar match {
                case ch if end contains ch ⇒
                    return sb.toString
                case ch if ees contains ch ⇒
                    if (foundEes)
                        throw new RuntimeException(
                            "found two ees in a number")
                    foundEes = true
                    sb.append(advance())
                    if (!(digits contains curChar))
                        throw new RuntimeException(
                            "'ee' must be followed by a number")
                case '.' ⇒
                    if (foundDot)
                        throw new RuntimeException(
                            "found two dots in a number")
                    foundDot = true
                    sb.append(advance())
                case ch if digits contains ch ⇒
                    sb.append(advance())
            }
        }
        throw new RuntimeException("unexpected end of input")
    }

    def readString(): String = {
        if (curChar != '"')
            throw new RuntimeException(
                s"""expected " but found: $curChar""")
        val sb = new StringBuilder
        sb.append(advance())
        while (curChar != '"') {
            if (curChar == '\\' && (
                input.charAt(idx + 1) == '\\' || input.charAt(idx + 1) == '"')
            ) sb.append(advance()).append(advance())
            else sb.append(advance())
        }
        sb.append(advance())
        sb.toString()
    }

    def readInsideArray(): Unit = {
        skipSpaces()
        readValue(inArray = true)
        skipSpaces()
        curChar match {
            case ',' ⇒
                out.println(advance())
                readInsideArray()
            case ']' ⇒
                out.println()
                indent -= 1
                out.print(curIndent + curChar)
                idx += 1
            case _ ⇒
                throw new RuntimeException(
                    s"expected , or ] but found: $curChar")
        }
    }

    def skipSpaces(): Unit = {
        while (idx < input.length && spaces.contains(curChar)) {
            idx += 1
        }
    }
}
