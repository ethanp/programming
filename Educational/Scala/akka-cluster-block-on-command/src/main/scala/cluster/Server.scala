package cluster

import akka.actor._

/**
 * Ethan Petuchowski
 * 4/9/15
 */
class Server extends Actor with ActorLogging {

    // I'm supposing these are the clients for whom this
    // server is responsible for informing of updates
    var clients = Set.empty[ActorRef]
    var nodeID = -1

    def receive = {
        case IDMsg(id) ⇒ nodeID = id
        case ClientConnected ⇒ clients += sender
    }
}

object Server {
    def main(args: Array[String]) = ClusterUtil joinClusterAs "server"
}
