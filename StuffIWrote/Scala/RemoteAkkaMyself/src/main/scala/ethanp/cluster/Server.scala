package ethanp.cluster

import akka.actor.{RootActorPath, Actor, Props, ActorSystem}
import akka.cluster.{Member, MemberStatus, Cluster}
import akka.cluster.ClusterEvent.{CurrentClusterState, MemberUp}
import com.typesafe.config.ConfigFactory

/**
 * Ethan Petuchowski
 * 4/9/15
 */
object Server {
    def main(args: Array[String]) = {
        // Override the configuration of the port when specified as program argument
        val port = if (args.isEmpty) "0" else args(0)
        val config = ConfigFactory.parseString(s"akka.remote.netty.tcp.port=$port").
          withFallback(ConfigFactory.parseString("akka.cluster.roles = [server]")).
          withFallback(ConfigFactory.load())

        val system = ActorSystem("ClusterSystem", config)
        system.actorOf(Props[Server], name = "server")
    }
}

class Server extends Actor {

  // get the Cluster owning the ActorSystem that this actor belongs to
  val cluster = Cluster(context.system)

  // subscribe to cluster changes, MemberUp
  // re-subscribe when restart
  override def preStart(): Unit = cluster.subscribe(self, classOf[MemberUp])
  override def postStop(): Unit = cluster.unsubscribe(self)

  def receive = {
    case ChatRequest(text) => sender ! ChatResult(text.toUpperCase)

    case state: CurrentClusterState =>
      state.members.filter(_.status == MemberStatus.Up) foreach register

    case MemberUp(m) => register(m)
  }

  def register(member: Member): Unit =
    if (member.hasRole("client"))
      context.actorSelection(RootActorPath(member.address) / "user" / "client") !
        ServerRegistration
}
