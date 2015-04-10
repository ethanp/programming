package ethanp.cluster

import java.util.Scanner

import akka.actor.{ActorLogging, Props, ActorSystem, Actor}
import akka.actor.Actor.Receive
import akka.cluster.ClusterEvent._
import akka.cluster.{Member, MemberStatus}
import com.typesafe.config.ConfigFactory

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

    def createNodeProc(role: Class[_]) =
        Process(s"sbt \"runMain ${role.getCanonicalName}} 0\"").lineStream
}

class Master extends Actor with ClusterNotificationReceiver with ActorLogging {
    override def receive = receiveRelevantMessage orElse receiveClusterNotification
    val sc = new Scanner(System.in)

    val childStreams = mutable.Set.empty[Stream[String]]

    new Thread {
        while (sc.hasNextLine) {
            val str = sc.nextLine
            val brkStr = str split " "
            log.info(s"handling { $str }")
            brkStr.head match {
                case "newClient" ⇒ childStreams.add(Master.createNodeProc(Client.getClass))
                case "newServer" ⇒ childStreams.add(Master.createNodeProc(Server.getClass))
                case "start" ⇒
                case "sendMessage" ⇒
            }
        }
    }

    def receiveRelevantMessage: Receive = {
        case ChatLog(string) ⇒ println(string)
    }
}

/**
 * Don't know of any reason to handle these here.
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
