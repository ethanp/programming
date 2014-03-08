package na.ethan.AkkaRetrieval

import akka.actor.{ActorSystem, ActorRef, Actor, Props}

/**
 * Ethan Petuchowski
 * 3/8/14
 */
class Retriever extends Actor {
    // TODO receive
    def receive: Actor.Receive = {
        case _ => None
    }
}

object HelloGDataWithAkka extends App {

    println("Hello World!")

    val system = ActorSystem(name = "helloGData")

    val retriever : ActorRef = system.actorOf(props=Props[Retriever], name="retriever")

    println("Final Line")
}
