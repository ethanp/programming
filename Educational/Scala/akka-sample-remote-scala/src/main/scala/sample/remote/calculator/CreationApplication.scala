package sample.remote.calculator

import scala.concurrent.duration._
import com.typesafe.config.ConfigFactory
import scala.util.Random
import akka.actor.ActorSystem
import akka.actor.Props

object CreationApplication {
  def main(args: Array[String]): Unit = {
    if (args.isEmpty || args.head == "CalculatorWorker")
      startRemoteWorkerSystem()
    if (args.isEmpty || args.head == "Creation")
      startRemoteCreationSystem()
  }

  def startRemoteWorkerSystem(): Unit = {

    /** An actor system is a hierarchical group of actors which share common configuration,
      * e.g. dispatchers, deployments, remote capabilities and addresses.
      * It is also the entry point for creating or looking up actors. */
    ActorSystem("CalculatorWorkerSystem", ConfigFactory.load("calculator"))
    println("Started CalculatorWorkerSystem")
  }

  def startRemoteCreationSystem(): Unit = {

    /** In this config, we specify the (Protocol, IPAddr, Port) to deploy children
      * of the "creationActor" created as a top-level actor in this ActorSystem. */
    val system = ActorSystem("CreationSystem", ConfigFactory.load("remotecreation"))
    val actor = system.actorOf(Props[CreationActor], name = "creationActor")

    println("Started CreationSystem")
    import system.dispatcher

    /* run the given command in 1 sec, then every subsequent second */
    system.scheduler.schedule(1.second, 1.second) {
      if (Random.nextInt(100) % 2 == 0)
        actor ! Multiply(Random.nextInt(20), Random.nextInt(20))
      else
        actor ! Divide(Random.nextInt(10000), Random.nextInt(99) + 1)
    }

  }
}
