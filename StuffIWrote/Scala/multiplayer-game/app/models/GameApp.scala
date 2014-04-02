package models

/**
 * Ethan Petuchowski
 * 3/30/14
 */

import akka.actor.{ActorRef, Props, Actor}
import play.api.libs.concurrent.Akka
import akka.pattern.ask
import play.api.Play.current
import scala.collection.mutable
import scala.concurrent.duration._
import scala.concurrent.Future
import akka.util.Timeout

/**
 * The server's Holder of the Games
 */
object GameApp {
  implicit val timeout = Timeout(5 seconds)

  // Map of title to Game
  // private means it has no public getter
  private var games = Map[String, ActorRef]()

  def createGame(user: String, name: String): Option[ActorRef] = {
    games.get(name) map { _ =>
      println(s"The name $name is already taken.")
      None
    } getOrElse {
      val newGame = Akka.system.actorOf(Props(new Game(name)))
      val gameFuture = newGame ? Join(user)  // will it properly ignore the response from the Game?
      // TODO use the gameFuture to tell the controller to render the new game for the user
      games += name -> newGame
      Some(newGame)
    }
  }

  def getGame(name: String): Option[ActorRef] = games.get(name)
  def gameSet: Set[String] = games.keySet
  def scoresForGame(name: String): Future[Any] = games.get(name).get ? Scores
}

/**
 * An instance of the Game
 */
case class Game(name: String) extends Actor {

  // Map of username to score
  val scoreboard = mutable.Map[String, Int]()

  def receive = {
    case Join(username) =>
      scoreboard.get(username) map { _ =>
        scoreboard += username -> 0
        sender ! Success
      } getOrElse {
        sender ! UsernameAlreadyTaken
      }
    case Point(username, addIt) =>
      scoreboard.get(username) map { oldScore =>
        val newScore = if (addIt) oldScore + 1 else oldScore - 1
        scoreboard.put(username, newScore)
        sender ! NewScore(scoreboard.toMap)
      } getOrElse {
        throw UserNotPlayingException(name, username) // I'm sure this is incorrect
      }
    case Scores =>
      sender ! scoreboard
  }
}

case class Point(username: String, addIt: Boolean)
case class Join(username: String)
case object Success
case object UsernameAlreadyTaken
case class NewScore(scoreboard: Map[String, Int])
case object Scores

// surely this is incorrect
case class UserNotPlayingException(gameTitle: String, username: String)
  extends Exception(username)
