package ethanp.cluster

import java.util.concurrent.atomic.AtomicInteger

import akka.actor._
import akka.cluster.Cluster
import akka.cluster.ClusterEvent.MemberUp
import akka.util.Timeout
import com.typesafe.config.ConfigFactory
import akka.pattern.ask
import scala.concurrent.duration._

/**
 * Ethan Petuchowski
 * 4/9/15
 */
class Client extends Actor {
  val cluster = Cluster(context.system)
  override def preStart(): Unit = cluster.subscribe(self, classOf[MemberUp])
  override def postStop(): Unit = cluster.unsubscribe(self)
  var servers = IndexedSeq.empty[ActorRef]
  var jobCounter = 0
  var myID = -1

  override def receive = {
    case job: ChatRequest if servers.isEmpty =>
      sender ! JobFailed("Service unavailable, try again later", job)

    case IDAssignment(id) ⇒ myID = id

    case job: ChatRequest =>
      jobCounter += 1
      servers(jobCounter % servers.size) forward job

    case MasterRegistration ⇒ sender ! ChatLog("CLIENT IS UP")

    case ServerRegistration if !servers.contains(sender()) =>
      context watch sender
      servers = servers :+ sender

    case Terminated(a) =>
      servers = servers.filterNot(_ == a)

    case MemberUp(m) ⇒ println(s"member up: ${m.address.toString.take(5)}")
  }
}

object Client {
  def main(args: Array[String]): Unit = {
    val system = Common.clusterSystem("client")
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
