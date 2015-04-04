package com.example.actors

import akka.actor.Actor
import akka.actor.ActorLogging

// Note: Usually the message object (GreeterMessages) and the actor class (GreeterActor) will be called the same thing (eg. Greeter)
object GreeterMessages {
  case object Greet
  case object Done
}

class GreeterActor extends Actor with ActorLogging {

  def receive = {
    case GreeterMessages.Greet => {
      var greetMsg = "Hello World!"

      println(greetMsg)
      log.info(greetMsg)

      sender() ! GreeterMessages.Done // Send the 'Done' message back to the sender
    }
  }

}