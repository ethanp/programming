import java.io.PrintStream

/**
  * Ethan Petuchowski
  * 2/19/16
  *
  * Note: a malformed string could raise AIOOBE
  */
object Formatter {

    /* TODO print the formatted JSON */

    var idx = 0
    var input = """{"a":"b"}"""
    val spaces = " \t\r\n".toSet
    var indent = 0
    val digits = ('0' to '9').toSet
    val numberPrefixes = "+-".toSet
    val out: PrintStream = System.out

    def curChar = input charAt idx

    def curIndent = " " * (2 * indent)

    def main(args: Array[String]): Unit = {
        for (i ← Seq(
//            """[234, 432, "\"abcd"]""",
            """[{"a":"b", "c" : [234E43, "3ed"]}, {"d": "e"}]"""
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
        curChar match {
            case '{' ⇒
                if (isValue) out.println(curChar)
                else out.println(curIndent+curChar)
                idx += 1
                indent += 1
                readInsideObject()
                true
            case _ ⇒
                false
        }
    }

    def matchArr(isValue: Boolean): Boolean = {
        curChar match {
            case '[' ⇒
                if (isValue) out.println(curChar)
                else out.println(curIndent+curChar)
                idx += 1
                indent += 1
                readInsideArray()
                true
            case _ ⇒
                false
        }
    }

    def readInsideObject(): Unit = {
        skipSpaces()
        out.print(curIndent+readString())
        skipSpaces()
        if (curChar != ':') {
            throw new RuntimeException(
                s"expected : but found: $curChar")
        }
        out.print(curChar)
        idx += 1
        skipSpaces()
        readValue(inArray = false)
        skipSpaces()
        curChar match {
            case ',' ⇒
                out.println(curChar)
                idx += 1
                readInsideObject()
            case '}' ⇒
                out.println()
                indent -= 1
                out.print(curIndent+curChar)
                idx += 1
                return
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
        sb.append(curChar)
        idx += 1
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
                    sb.append(curChar)
                    idx += 1
                    if (!(digits contains curChar))
                        throw new RuntimeException(
                            "'ee' must be followed by a number")
                case '.' ⇒
                    if (foundDot)
                        throw new RuntimeException(
                            "found two dots in a number")
                    foundDot = true
                    sb.append(curChar)
                    idx += 1
                case ch if digits contains ch ⇒
                    sb.append(curChar)
                    idx += 1
            }
        }
        throw new RuntimeException("unexpected end of input")
    }

    def readString(): String = {
        if (idx == input.length) {
            throw new RuntimeException(
                "expected \" but reached end of input")
        }
        if (curChar != '"') {
            throw new RuntimeException(
                s"""expected " but found: $curChar""")
        }
        val sb = new StringBuilder("\"")
        idx += 1
        while (curChar != '"') {
            if (idx == input.length) {
                throw new RuntimeException(
                    "expected \" but reached end of input")
            }
            if (curChar == '\\') {
                val nextChar = input.charAt(idx+1)
                if (nextChar == '\\' || nextChar == '"') {
                    sb.append(curChar).append(input.charAt(idx+1))
                    idx += 2
                }
            }
            else {
                sb.append(curChar)
                idx += 1
            }
        }
        sb.append(curChar)
        idx += 1
        sb.toString()
    }

    def readInsideArray(): Unit = {
        skipSpaces()
        readValue(inArray = true)
        skipSpaces()
        curChar match {
            case ',' ⇒
                out.println(curChar)
                idx += 1
                readInsideArray()
            case ']' ⇒
                out.println()
                indent -= 1
                out.print(curIndent+curChar)
                idx += 1
                return
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
