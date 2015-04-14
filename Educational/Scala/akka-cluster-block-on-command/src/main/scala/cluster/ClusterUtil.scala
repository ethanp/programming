package cluster

import akka.actor._
import akka.cluster.Member
import com.typesafe.config.ConfigFactory

/**
 * Ethan Petuchowski
 * 4/10/15
 */
object ClusterUtil {
    type NodeID = Int
    def joinClusterAs(role: String): ActorRef = ClusterUtil.joinClusterAs("0", role)
    def joinClusterAs(port: String, role: String) = {
        val config = ConfigFactory.parseString(s"akka.remote.netty.tcp.port=$port").
          withFallback(ConfigFactory.parseString(s"akka.cluster.roles = [$role]")).
          withFallback(ConfigFactory.load())
        val s = ActorSystem("ClusterSystem", config)
        role match {
            case "server" ⇒ s.actorOf(Props[Server], name = "server")
            case "client" ⇒ s.actorOf(Props[Client], name = "client")
            case "master" ⇒ s.actorOf(Props[Master], name = "master")
        }
    }
    def getPath(m: Member) = RootActorPath(m.address) / "user" / m.roles.head
    def getSelection(path: ActorPath, context: ActorContext) = context actorSelection path
}
