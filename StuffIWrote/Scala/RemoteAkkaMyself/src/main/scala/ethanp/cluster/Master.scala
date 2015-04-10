package ethanp.cluster

import java.util.Scanner

import akka.actor._
import akka.actor.Actor.Receive
import akka.cluster.ClusterEvent._
import akka.cluster.{Cluster, Member, MemberStatus}
import com.typesafe.config.ConfigFactory
import sys.process._ // package with implicits for running external processes

import scala.collection.mutable
import scala.sys.process.Process

/**
 * Ethan Petuchowski
 * 4/9/15
 *
 * Receives commands from the command line and deals with them appropriately.
 *
 * Also the "first seed node" i.e. the one that all actors attempting
 * to join the cluster contact first.
 */
object Master extends App {

    /* make the master the first seed node */

    val port = "2551" // first seed port
    val name = "master"
    val system = Common.clusterSystem(port, name)
    system.actorOf(Props[Master], name = name)

    // starting 2 clients and 3 servers (in this Process, but as separate Actors)
    Client.main(Seq("2552").toArray) // let a client be another seed node (won't crash)
    Server.main(Array.empty)
    Server.main(Array.empty)
    Server.main(Array.empty)
    Client.main(Array.empty)

    // gotta love scala.
    def createClient() = "sbt execClient".run()
    def createServer() = "sbt execServer".run()

    val sc = new Scanner(System.in)
    val childProcs = mutable.Set.empty[Process]
    new Thread {
        while (sc.hasNextLine) {
            val str = sc.nextLine
            val brkStr = str split " "
            println(s"handling { $str }")
            brkStr.head match {
                case "newClient" ⇒ childProcs.add(Master.createClient())
                case "newServer" ⇒ childProcs.add(Master.createServer())
                case "start" ⇒
                case "sendMessage" ⇒
            }
        }
        childProcs.foreach(_.destroy())
    }
//    createClient()
}

class Master extends Actor with ActorLogging {

    val cluster = Cluster(context.system)
    override def preStart(): Unit = cluster.subscribe(self, classOf[MemberUp])
    override def postStop(): Unit = cluster.unsubscribe(self)

    override def receive = {
        case ChatLog(string) ⇒ println(string)

        case state: CurrentClusterState =>
            state.members.filter(_.status == MemberStatus.Up) foreach register

        case MemberUp(m) => register(m)
    }

    def register(m: Member) = {
        log.info(s" master registering ${m.address}")
        if (m.hasRole("client"))
            context.actorSelection(RootActorPath(m.address)/"user"/"client") ! MasterRegistration
    }
}

/**
 * Don't know of any reason to handle these here (I'm not using it now).
 * The code is here so I don't forget the option to handle these events exists.
 */
trait ClusterNotificationReceiver {
    def receiveClusterNotification: Receive = {
        // snapshot of the full cluster state (e.g. membership)
        // sent to the subscriber as their first message
        case state: CurrentClusterState =>
            state.members.filter(_.status == MemberStatus.Up) foreach noteAlreadyUp

        // newly joined member's status has been changed to "Up"
        // the parameter type "Member" represents a cluster member node
        case MemberUp(m) =>


        /* Note: I don't think i'm currently "subscribed" to any of these */

        // member is leaving, status has been changed to "Exiting"
        case MemberExited(m) ⇒

        // member has been removed from the cluster
        case MemberRemoved(m, s) ⇒

        // member has been detected as unreachable by failure detector of ≥ 1 other node
        case UnreachableMember(m) ⇒

        // member is considered reachable again after having been unreachable
        case ReachableMember(m) ⇒
    }
    def noteAlreadyUp(member: Member): Unit = println(s"${member.address} is already up")
}
