import akka.actor.{ ActorRef, ActorSystem, Props, Actor, Inbox }
import scala.concurrent.duration._

case object Greet
case class WhoToGreet(who: String)
case class Greeting(message: String)

class Greeter extends Actor {
  var greeting = ""

  def receive = {
    case WhoToGreet(who) => greeting = s"hello, $who"
    case Greet           => sender ! Greeting(greeting) // Send the current greeting back to the sender
  }
}

object HelloAkkaScala extends App {

  // Create the 'helloakka' actor system
  val system = ActorSystem(name="helloakka")

  // Create the 'greeter' actor
  val greeter: ActorRef = system.actorOf(props=Props[Greeter], name="greeter")

  // Create an "actor-in-a-box"
  val inbox: Inbox = Inbox.create(system)

  // Tell the 'greeter' to change its 'greeting' message
  greeter.tell(msg=WhoToGreet("akka"), sender=ActorRef.noSender)

  // Ask the 'greeter for the latest 'greeting'
  // Reply should go to the "actor-in-a-box"
  inbox.send(target=greeter, msg=Greet)

  // Wait 5 seconds for the reply with the 'greeting' message
  val Greeting(message1: String) = inbox.receive(max=5.seconds)
  println(s"Greeting: $message1")

  // Change the greeting and ask for it again
  greeter.tell(WhoToGreet("typesafe"), ActorRef.noSender)
  inbox.send(greeter, Greet)
  val Greeting(message2) = inbox.receive(max=5.seconds)
  println(s"Greeting: $message2")

  val greetPrinter: ActorRef = system.actorOf(Props[GreetPrinter])

  // after zero seconds, send a Greet message every second to the greeter
  // with a sender of the greetPrinter
  system.scheduler.schedule(initialDelay=0.seconds, interval=1.second, receiver=greeter, message=Greet)(executor=system.dispatcher, sender=greetPrinter)

}

// prints a greeting
class GreetPrinter extends Actor {
  def receive = {
    case Greeting(message) => println(message)
  }
}
