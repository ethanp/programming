package cluster

import akka.actor._

/**
 * Ethan Petuchowski
 * 4/9/15
 */
class Client extends Actor {

  // we're assuming client can only connect to a SINGLE server
  var server: ActorSelection = _

  override def receive = {
    case ServerPath(path) ⇒
      server = ClusterUtil.getSelection(path, context)
      server ! ClientConnected
  }
}

object Client {
  def main(args: Array[String]): Unit = ClusterUtil joinClusterAs "client"
}
