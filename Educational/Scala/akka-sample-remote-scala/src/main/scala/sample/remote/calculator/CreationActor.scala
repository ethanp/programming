package sample.remote.calculator

import akka.actor.{Actor, Props}

/**
 * Creates a child CalculatorActor for each incoming MathOp msg.
 * Then when it receives the result, it (async-ly) stops the CalcActor.
 */
class CreationActor extends Actor {

  def receive = {
    case op: MathOp =>
      val calculator = context.actorOf(Props[CalculatorActor])
      calculator ! op
    case result: MathResult => result match {
      case MultiplicationResult(n1, n2, r) =>
        printf("Mul result: %d * %d = %d\n", n1, n2, r)
        context.stop(sender())
      case DivisionResult(n1, n2, r) =>
        printf("Div result: %.0f / %d = %.2f\n", n1, n2, r)
        context.stop(sender())
    }
  }
}
