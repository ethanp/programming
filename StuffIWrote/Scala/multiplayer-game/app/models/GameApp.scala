package models

/**
 * Ethan Petuchowski
 * 3/30/14
 */

import akka.actor.{ActorRef, Props, Actor}
import play.api.libs.concurrent.Akka
import play.api.Play.current
import scala.collection.mutable

/**
 * The server's Holder of the Games
 */
object GameApp {

  // Map of title to Game
  // I'm using a var instead of a mutable.Map so that if
  // I pass this thing around, I can (maybe?) prevent other
  // users from modifying it
  var games = Map[String, ActorRef]()

  def createGame(user: String, name: String): Option[ActorRef] = {
    games get name map { _ =>
      println(s"The name $name is already taken.")
      None
    } getOrElse {
      val newGame = Akka.system.actorOf(Props(new Game(name)))
      newGame ! Join(user)  // will it properly ignore the response from the Game?
      games += name -> newGame
      Some(newGame)
    }
  }

  def getGame(name: String): Option[ActorRef] = games get name
}

/**
 * An instance of the Game
 */
case class Game(name: String) extends Actor {

  // Map of username to score
  val scoreboard = mutable.Map[String, Int]()

  def receive = {
    case Join(username) =>
      scoreboard get username map { _ =>
        scoreboard += username -> 0
        sender ! Success
      } getOrElse {
        sender ! UsernameAlreadyTaken
      }
    case Point(username, addIt) =>
      scoreboard get username map { oldScore =>
        val newScore = if (addIt) oldScore + 1 else oldScore - 1
        scoreboard.put(username, newScore)
        sender ! NewScore(scoreboard.toMap)
      } getOrElse {
        throw UserNotPlayingException(name, username) // I'm sure this is incorrect
      }
  }
}

case class Point(username: String, addIt: Boolean)
case class Join(username: String)
case object Success
case object UsernameAlreadyTaken
case class NewScore(scoreboard: Map[String, Int])

// surely this is incorrect
case class UserNotPlayingException(gameTitle: String, username: String)
  extends Exception(username)
