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
import scala.concurrent.{ExecutionContext, Await, Future}
import akka.util.Timeout
import ExecutionContext.Implicits.global

/**
 * The server's Holder of the Games
 */
object GameApp {
  implicit val timeout = Timeout(5.seconds)

  // Map of title to Game
  // private means it has no public getter
  private var games = Map[String, ActorRef]()

  /** create, but don't join, Game aktor */
  def createGame(name: String): Option[ActorRef] = {
    games.get(name) map { _ =>
      println(s"createGame: The name $name is already taken.")
      None
    } getOrElse {
      val newGame = Akka.system.actorOf(Props(classOf[Game], name))
      games += name -> newGame
      Some(newGame)
    }
  }

  def getGame(name: String): Option[ActorRef] = games.get(name)

  def gameSet: Set[String] = games.keySet

  def scoresForGame(name: String): Option[Map[String, Int]] = {
    val yada = games.get(name) map {
      case gameRef: ActorRef => Some(gameRef ? Scores)
    } getOrElse { None }
    yada map { y: Future[Any] =>
      Await.result(y, 1.second).asInstanceOf[Map[String, Int]]
    }
  }

  def joinGame(user: String, name: String): Future[Any] = {
    games.get(name) map {
      case gameRef: ActorRef => (gameRef ? Join(user)) map {
        case Success => for (g <- gameRef ? Scores) yield g
        case a@UsernameAlreadyTaken => a
      }
    } getOrElse Future(GameDoesntExist)
  }
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
case object GameDoesntExist
case class NewScore(scoreboard: Map[String, Int])
case object Scores

// surely this is incorrect
case class UserNotPlayingException(gameTitle: String, username: String)
  extends Exception(username)
