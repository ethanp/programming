package ethanp.cluster

import java.util.concurrent.atomic.AtomicInteger

import akka.actor._
import akka.util.Timeout
import com.typesafe.config.ConfigFactory
import akka.pattern.ask
import scala.concurrent.duration._

/**
 * Ethan Petuchowski
 * 4/9/15
 */
class Client extends Actor {

  var servers = IndexedSeq.empty[ActorRef]
  var jobCounter = 0

  def receive = {
    case job: ChatRequest if servers.isEmpty =>
      sender ! JobFailed("Service unavailable, try again later", job)

    case job: ChatRequest =>
      jobCounter += 1
      servers(jobCounter % servers.size) forward job

    case ServerRegistration if !servers.contains(sender()) =>
      context watch sender
      servers = servers :+ sender

    case Terminated(a) =>
      servers = servers.filterNot(_ == a)
  }
}

object Client {
  def main(args: Array[String]): Unit = {
    // Override the configuration of the port when specified as program argument
    val port = if (args.isEmpty) "0" else args(0)
    val config = ConfigFactory.parseString(s"akka.remote.netty.tcp.port=$port").
      withFallback(ConfigFactory.parseString("akka.cluster.roles = [client]")).
      withFallback(ConfigFactory.load())

    val system = ActorSystem("ClusterSystem", config)
    val client = system.actorOf(Props[Client], name = "client")

    val counter = new AtomicInteger
    import system.dispatcher
    system.scheduler.schedule(2.seconds, 2.seconds) {
      implicit val timeout = Timeout(5 seconds)
      (client ? ChatRequest("hello-" + counter.incrementAndGet())) onSuccess {
        case result => println(result)
      }
    }
  }
}
