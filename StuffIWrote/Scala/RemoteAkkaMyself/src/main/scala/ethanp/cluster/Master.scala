package ethanp.cluster

import java.util.Scanner

import akka.actor._
import akka.actor.Actor.Receive
import akka.cluster.ClusterEvent._
import akka.cluster.{Cluster, Member, MemberStatus}
import com.typesafe.config.ConfigFactory
import ethanp.cluster.IDAssignment
import scala.concurrent.Future
import scala.util.{Failure, Success}
import sys.process._ // package with implicits for running external processes
import akka.pattern.ask

import scala.collection.mutable
import scala.sys.process.Process
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

    /* make the master the first seed node */

    val master = Common.clusterSystem("2551", "master").actorOf(Props[Master], name = "master")

    // starting 2 clients and 3 servers (in this Process, but as separate Actors)
    Client.main(Array.empty) // let a client be another seed node (won't crash)
    Server.main(Array.empty)
    Server.main(Array.empty)
    Server.main(Array.empty)
    Client.main(Array.empty)

    // gotta love scala.
    def createClient(id: Int) = s"sbt execClient $id".run()
    def createServer(id: Int) = s"sbt execServer $id".run()
//    createClient()

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
}

class Master extends Actor with ActorLogging {

    val cluster = Cluster(context.system)

    override def preStart(): Unit = cluster.subscribe(self,
                 classOf[MemberUp], classOf[MemberRemoved])

    override def postStop(): Unit = cluster.unsubscribe(self)

    val servers = mutable.Map.empty[Int, Member]
    val clients = mutable.Map.empty[Int, Member]

    def firstFreeID(set: Set[Int]) = Stream.from(0).filterNot(set.contains).head
    def firstFreeServerID = firstFreeID(servers.keySet.toSet)
    def firstFreeClientID = firstFreeID(clients.keySet.toSet)

    override def receive = {
        case ChatLog(string) ⇒ println(string)

        case state: CurrentClusterState =>
            state.members.filter(_.status == MemberStatus.Up) foreach store

        case MemberUp(m) => store(m)

        /* member has been removed from the cluster
         * time it takes to go from "unreachable" to "down" (and therefore removed)
         * is configured by e.g. "auto-down-unreachable-after = 3s"     */
        case MemberRemoved(m, prevStatus) ⇒ remove(m)
    }

    def getPath(m: Member) = context.actorSelection(RootActorPath(m.address)/"user"/m.roles.head)

    def store(m: Member) = {
        m.roles.head match {
            case "client" ⇒
                val id: Int = firstFreeClientID
                getPath(m) ! IDAssignment(id)
                clients.put(id, m)

            case "server" ⇒
                val id: Int = firstFreeServerID
                getPath(m) ! IDAssignment(id)
                servers.put(id, m)
        }
    }

    def remove(m: Member) = {
        m.roles.head match {
            case "client" ⇒ clients retain ((k,v) ⇒ m != v)
            case "server" ⇒ servers retain ((k,v) ⇒ m != v)
        }
    }
}
