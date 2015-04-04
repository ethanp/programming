package sample.remote.calculator

import scala.concurrent.duration._
import akka.actor.Actor
import akka.actor.ActorIdentity
import akka.actor.ActorRef
import akka.actor.Identify
import akka.actor.ReceiveTimeout
import akka.actor.Terminated

/**
 * sends operations to the remote calculator service (viz. `CalculatorActor`)
 * @param path full path including the remote address of the calculator service
 *             "akka.tcp://CalculatorSystem@127.0.0.1:2552/user/calculator"
 * The intermediate dir /user/ is the supervisor actor of all top-level actors
 */
class LookupActor(path: String) extends Actor {

  sendIdentifyRequest()

  def sendIdentifyRequest(): Unit = {
    /** On construction, send an Identify message to the given path (transcribed above)
      * It will reply with ActorIdentity, containing its ActorRef ('received' below)
      * This response to Identify is provided by default by actors.
      */
    context.actorSelection(path) ! Identify(path)
    import context.dispatcher
    /** Schedule this actor to receive a ReceiveTimeout after 3 seconds.
      * If all goes according to plan, we will `become(identifying)` before that though. */
    context.system.scheduler.scheduleOnce(3.seconds, self, ReceiveTimeout)
  }

  def receive = identifying

  def identifying: Actor.Receive = {
    case ActorIdentity(`path`, Some(actor)) =>
      /** Registers this actor as a Monitor for the provided ActorRef.
        * This actor will receive a Terminated(subject) message when watched actor is terminated. */
      context.watch(actor)

      /** Changes the Actor's behavior to become
        * the new 'Receive' (PartialFunction[Any, Unit]) handler.
        * Replaces the current behavior on the top of the behavior stack. */
      context.become(active(actor))
    case ActorIdentity(`path`, None) => println(s"Remote actor not available: $path")
    case ReceiveTimeout              => sendIdentifyRequest()
    case _                           => println("Not ready yet")
  }

  def active(actor: ActorRef): Actor.Receive = {
    case op: MathOp => actor ! op
    case result: MathResult => result match {
      case AddResult(n1, n2, r) =>
        printf("Add result: %d + %d = %d\n", n1, n2, r)
      case SubtractResult(n1, n2, r) =>
        printf("Sub result: %d - %d = %d\n", n1, n2, r)
    }
    /** we'll receive this if `actor` dies because we registered
      * as a Monitor using `watch(actor)` above */
    case Terminated(`actor`) =>
      println("Calculator terminated")
      sendIdentifyRequest()
      context.become(identifying)
    case ReceiveTimeout =>
    // ignore

  }
}
