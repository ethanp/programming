/**
  * Ethan Petuchowski
  * 2/19/16
  *
  * Note: a malformed string could raise AIOOBE
  */
object Formatter {

    var idx = 0
    var input = """{"a":"b"}"""
    val spaces = " \t\r\n".toSet
    var indent = 0
    val digits = ('0' to '9').toSet
    val numberPrefixes = "+-".toSet

    def curChar = input charAt idx

    def curIndent = " " * (4 * indent)

    def main(args: Array[String]): Unit = {
        for (i ← Seq("""{"a":"b"}""", """{"a": -234}""", """[234, 432, "\"abcd"]""")) {
            idx = 0
            indent = 0
            input = i
            parse()
        }
    }


    def parse(): Unit = {
        skipSpaces()
        if (!(matchObj() || matchArr()))
            throw new RuntimeException(
                s"expected { or [ but found: $curChar")
        skipSpaces()
        if (idx != input.length)
            throw new RuntimeException(
                s"expected end of input but found: $curChar")
    }

    def matchObj(): Boolean = {
        curChar match {
            case '{' ⇒
                idx += 1
                indent += 1
                readInsideObject()
                true
            case _ ⇒
                false
        }
    }

    def matchArr(): Boolean = {
        curChar match {
            case '[' ⇒
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
        readString()
        skipSpaces()
        if (curChar != ':') {
            throw new RuntimeException(
                s"expected : but found: $curChar")
        }
        idx += 1
        skipSpaces()
        readValue()
        skipSpaces()
        curChar match {
            case ',' ⇒
                idx += 1
                readInsideObject()
            case '}' ⇒
                idx += 1
                return
            case _ ⇒
                throw new RuntimeException(
                    s"expected , or } but found: $curChar")
        }
    }

    def readValue(): Unit = {
        curChar match {
            case '{' ⇒ matchObj()
            case '[' ⇒ matchArr()
            case ch if (digits | numberPrefixes) contains ch ⇒ readNumber()
            case '"' ⇒ readString()
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
        readValue()
        skipSpaces()
        curChar match {
            case ',' ⇒
                idx += 1
                readInsideArray()
            case ']' ⇒
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
