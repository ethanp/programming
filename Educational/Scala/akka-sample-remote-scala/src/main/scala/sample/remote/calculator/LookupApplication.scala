package sample.remote.calculator

import scala.concurrent.duration._
import scala.util.Random
import com.typesafe.config.ConfigFactory
import akka.actor.ActorSystem
import akka.actor.Props

object LookupApplication {
  def main(args: Array[String]): Unit = {
    if (args.isEmpty || args.head == "Calculator")
      startRemoteCalculatorSystem()
    if (args.isEmpty || args.head == "Lookup")
      startRemoteLookupSystem()
  }

  def startRemoteCalculatorSystem(): Unit = {
    /**
     * EP: we create an ActorSystem called 'CalculatorSystem',
     * whose configuration is loaded from 'main/resources/calculator.conf'
     * Note that config also loads 'common.conf', which specifies
     *    - the IP-addr
     *    - the "actor provider", which
     *        1) starts up the actor on a remote node, and
     *        2) creates a RemoteActorRef representing it, which
     *            a) is an ActorRef, which is an
     *                * Immutable and serializable handle to an actor,
     *                  which may or may not reside on the local host
     *                  or inside the same ActorSystem
     *            b) but in particular does NOT reside within the same ActorSystem (aka. "remote")
     */
    val system = ActorSystem("CalculatorSystem", ConfigFactory.load("calculator"))

    /**
     * EP: init & run a CalculatorActor named "calculator" in the CalculatorSystem
     * the port this actor listens on is specified in the config file
     */
    system.actorOf(Props[CalculatorActor], "calculator")

    println("Started CalculatorSystem - waiting for messages")
  }

  def startRemoteLookupSystem(): Unit = {
    val system = ActorSystem("LookupSystem", ConfigFactory.load("remotelookup"))
    val remotePath = "akka.tcp://CalculatorSystem@127.0.0.1:2552/user/calculator"
    val actor = system.actorOf(Props(classOf[LookupActor], remotePath), "lookupActor")

    println("Started LookupSystem")
    import system.dispatcher
    system.scheduler.schedule(1.second, 1.second) {
      if (Random.nextInt(100) % 2 == 0)
        actor ! Add(Random.nextInt(100), Random.nextInt(100))
      else
        actor ! Subtract(Random.nextInt(100), Random.nextInt(100))
    }
  }
}
