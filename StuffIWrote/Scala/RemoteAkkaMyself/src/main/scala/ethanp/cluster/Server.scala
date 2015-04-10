package ethanp.cluster

import akka.actor.{Actor, ActorSystem, Props, RootActorPath}
import akka.cluster.ClusterEvent._
import akka.cluster.{Cluster, Member, MemberStatus}
import com.typesafe.config.ConfigFactory

/**
 * Ethan Petuchowski
 * 4/9/15
 */
object Server {
    def main(args: Array[String]) = {
        // Override the configuration of the port when specified as program argument
        val port: String = if (args.isEmpty) "0" else args(0)
        val name = "server"
        val system = Common.clusterSystem(port, name)
        system.actorOf(Props[Server], name = name)
    }
}

class Server extends Actor {

    /* O.G: "get the Cluster owning the ActorSystem that this actor belongs to"
     * E.P: ...by contacting the "seed nodes" spec'd in the config (repeatedly until one responds).
     *   I think this means this nodes entire ActorSystem is going to
     *     become a part of the Cluster OF Actor Systems!
     */
    val cluster = Cluster(context.system)

    // subscribe to cluster changes, MemberUp
    // re-subscribe when restart
    override def preStart(): Unit = cluster.subscribe(self, classOf[MemberUp])

    override def postStop(): Unit = cluster.unsubscribe(self)

    def receive = {
        case ChatRequest(text) => sender ! ChatResult(text.toUpperCase)

        // snapshot of the full cluster state (e.g. membership)
        // sent to the subscriber as their first message
        case state: CurrentClusterState =>
            state.members.filter(_.status == MemberStatus.Up) foreach register

        // newly joined member's status has been changed to "Up"
        // the parameter type "Member" represents a cluster member node
        case MemberUp(m) => register(m)
    }

    def register(member: Member): Unit =
        // recall that a node has a SET of roles, this just checks if eg. "client" is one of them
        if (member.hasRole("client")) {
            context.actorSelection(RootActorPath(member.address) / "user" / "client") !
                    ServerRegistration
        }
}
