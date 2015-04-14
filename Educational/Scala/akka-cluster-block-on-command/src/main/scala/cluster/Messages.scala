package cluster

import akka.actor.ActorPath

/**
 * Ethan Petuchowski
 * 4/9/15
 */
case class ServerPath(actorPath: ActorPath)
case object ClientConnected

case object Done
case object Pause
case object Start
case object TakeAWhile
case class PrintID(id: Int)
case class IDMsg(id: Int)
