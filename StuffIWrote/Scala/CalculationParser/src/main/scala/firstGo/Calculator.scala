package firstGo

/**
 * Ethan Petuchowski
 * 8/15/15
 *
 * I have no idea what I'm doing, but perhaps I can still get this thing to work.
 *
 * The point is it takes in a string and "interprets" it, e.g.
 *
 *      3               => 3
 *      3 + 4           => 7
 *      ( 1 + 2 ) * 3   => 9
 *
 *      etc.
 *
 * I guess the hard part right now, is getting it to properly build the parse tree
 * out of the realistic "infix" notation. But hey, it's a problem that many people
 * have solved before me, and I want to try to figure it out. Meanwhile, I'm building
 * in a certain amount of "genericicity" in the code just for kicks, or maybe just
 * because the Spark dudes did that and that was cool.
 */
object Calculator extends Interpreter[Double, CalculatorTokenSet] {

    /**
     * Expects all tokens to be separated by a space.
     * Also simply blows up on invalid input right now.
     * We'd have to return an array of options or something.
     */
    def lexicallyAnalyze(input: String): Seq[CalculatorTokenSet] = {
        input split " " map {
            case Plus.repr => Plus
            case Minus.repr => Minus
            case Times.repr => Times
            case Divide.repr => Divide
            case x => Number(x.toDouble)
        }
    }

    def closeTo(d1: Double, d2: Double): Boolean = (d1-d2).abs < .1

    def runTests(): Unit = {
        // TODO there's some way to do plusMinus as an infix "+-",
        // but I'm not sure what to import to get that to work.
        assert(closeTo(evaluate("3"), 3))
    }

    def main(args: Array[String]) {
        runTests()
    }

    override def parser(tokens: Seq[CalculatorTokenSet]): ASTNode[CalculatorTokenSet] = {
        ASTNode(LeftParen, Vector(ASTNode(RightParen, Nil)))
    }

    override def isSemanticallyCorrect(ast: ASTNode[CalculatorTokenSet]): Boolean = true

    override def interpret(ast: ASTNode[CalculatorTokenSet]): Double = {
        3
    }
}

trait Interpreter[Output, Token <: TokenSet] {
    def lexicallyAnalyze(input: String): Seq[Token]
    def parser(tokens: Seq[Token]): ASTNode[Token]
    def isSemanticallyCorrect(ast: ASTNode[Token]): Boolean
    def interpret(ast: ASTNode[Token]): Output

    def evaluate(input: String): Output = {
        val tokens: Seq[Token] = lexicallyAnalyze(input)
        val ast: ASTNode[Token] = parser(tokens)
        if (!isSemanticallyCorrect(ast)) throw new IllegalStateException()
        /* My parse tree is the AST, so I don't need a code generator */
        interpret(ast)
    }
}

trait TokenSet {
    val repr: String
}

sealed trait CalculatorTokenSet extends TokenSet
sealed trait Operator extends CalculatorTokenSet
sealed trait Paren extends CalculatorTokenSet
case object Plus extends Operator {
    override val repr: String = "+"
}
case object Minus extends Operator {
    override val repr: String = "+"
}
case object Times extends Operator {
    override val repr: String = "*"
}
case object Divide extends Operator {
    override val repr: String = "/"
}
case class Number(value: Double) extends CalculatorTokenSet {
    override val repr: String = value.toString
}
case object LeftParen extends Paren {
    override val repr: String = "("
}
case object RightParen extends Paren {
    override val repr: String = ")"
}

case class ASTNode[T <: TokenSet] (
    value: T,
    children: Seq[ASTNode[T]]
)
