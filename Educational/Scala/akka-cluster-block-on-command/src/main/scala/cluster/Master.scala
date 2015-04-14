package cluster

import java.lang.System.err
import java.util.Scanner

import akka.actor._
import akka.cluster.ClusterEvent._
import akka.cluster.{Cluster, Member}
import akka.util.Timeout
import cluster.ClusterUtil.{NodeID, getPath, getSelection}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent._
import scala.concurrent.duration._

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

    /**
     * Create a new Client Actor (inside this process) that will join the macro-cluster.
     * It won't know it's console-ID yet, we'll tell it that once it joins the cluster.
     */
    def createClient(cid: NodeID, sid: NodeID): Future[Boolean] = {
        clientID = cid
        serverID = sid
        Client.main(Array.empty)
        Future(true)
    }

    /**
     * Create a new Server Actor (inside this process) that will join the macro-cluster.
     * It won't know it's console-ID yet, we'll tell it that once it joins the cluster.
     */
    def createServer(sid: NodeID): Future[Boolean] = {
        serverID = sid
        Server.main(Array.empty)
        Future(true) // TODO I want this to return a future that I have to complete later
    }

    /**
     * Make the Master Actor the first seed node in the Cluster, i.e. the one standing by
     * waiting for new nodes to ask to join the cluster so that it can say a resounding YES!
     */
    val clusterKing = ClusterUtil.joinClusterAs("2551", "master")

    /**
     * the client ID that will be assigned to the next Client to join the cluster
     */
    var clientID: NodeID = -1

    /**
     * the server ID that will be assigned to the next Server to join the cluster
     */
    var serverID: NodeID = -1

    /* THE COMMAND LINE INTERFACE */
    val sc = new Scanner(System.in)
    new Thread {
        if (sc hasNextLine) handleNext
        else System exit 0
    }

    /**
     * we only handle one line at a time,
     * otherwise we are in free-space simply waiting for it to complete,
     * in the callback handler, we try to handle the next line
     */
    def handleNext = {
        println("handling next")
        handle(sc nextLine)
    }

    implicit val timeout = Timeout(30 seconds)

    /**
     * Sends command-line commands to the Master Actor as messages.
     * Would theoretically work even if the CLI and Master were on different continents.
     */
    def handle(str: String) = {
        val brkStr = str split " "
        println(s"handling { $str }")
        lazy val b1 = brkStr(1) // 'lazy' because element does not exist unless it is needed
        lazy val b2 = brkStr(2)
        lazy val b1i = b1.toInt
        lazy val b2i = b2.toInt
        brkStr head match {
            case "joinServer" ⇒ createServer(b1i)
            case "joinClient" ⇒ createClient(b1i, b2i)
            case "pause"      ⇒ clusterKing ! Pause
            case "start"      ⇒ clusterKing ! Start
            case "takeAWhile" ⇒ clusterKing ! TakeAWhile
            case "printLog"   ⇒ clusterKing ! PrintID(id = b1i)
        }
    }
}

class Master extends Actor with ActorLogging {
    val cluster = Cluster(context.system)
    override def preStart() = cluster.subscribe(self, classOf[MemberUp])
    override def postStop() = cluster.unsubscribe(self)
    def selFromMember(m: Member): ActorSelection = getSelection(getPath(m), context)
    var members = Map.empty[Int, Member]

    override def receive = {
        case Pause        ⇒
            Thread sleep 3000
            println("ready")
            Master.handleNext
        case Start        ⇒ Master.handleNext
        case TakeAWhile   ⇒
        case PrintID(id)  ⇒
        case MemberUp(m)  ⇒ assignMemberID(m)
    }

    def assignMemberID(m: Member): Unit = {
        m.roles.head match {
            case "client" ⇒
                val cid: Int = Master.clientID
                val sid: Int = Master.serverID

                if (cid == -1) { err println "cid hasn't been set"; return }
                if (sid == -1) { err println "sid hasn't been set"; return }
                if (members contains cid) { err println s"Node $cid already exists"; return }
                if (!(members contains sid)) { err println s"Node $sid doesn't exist"; return }

                members += (cid → m)  // save reference to this member

                // tell the client the server it is supposed to connect to
                getSelection(getPath(m), context) ! ServerPath(getPath(members(Master.serverID)))

            case "server" ⇒
                val sid: Int = Master.serverID

                if (sid == -1) { err println "sid hasn't been set"; return }
                if (members contains sid) { err println s"Node $sid already exists"; return }

                members += (sid → m)
                selFromMember(m) ! IDMsg(sid)

            case "master" ⇒ log.info("ignoring Master MemberUp")
        }
    }

    def remove(m: Member) { members = members filterNot { case (k, v) ⇒ m == v } }
}
