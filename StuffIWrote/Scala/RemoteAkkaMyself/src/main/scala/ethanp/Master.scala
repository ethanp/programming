package ethanp

import akka.actor.{Props, Actor}
import akka.actor.Actor.Receive

/**
 * Ethan Petuchowski
 * 4/7/15
 */
class Master extends Actor {
    override def preStart(): Unit = {
        val server = context.actorOf(Props[Server])
        server ! Init(2, 3)
    }
    override def receive: PartialFunction[Any, Unit] = {
        case _ ⇒ println("OK")
    }
}
