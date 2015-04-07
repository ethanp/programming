package ethanp

import akka.actor.Actor
import akka.actor.Actor.Receive

/**
 * Ethan Petuchowski
 * 4/7/15
 */
class Server extends Actor {
    override def receive: PartialFunction[Any, Unit] = {
        case Init(numClients, numServers) ⇒
            println(s"numC: $numClients, numS: $numServers")
    }
}

case class Init(numClients: Int, numServers: Int)
