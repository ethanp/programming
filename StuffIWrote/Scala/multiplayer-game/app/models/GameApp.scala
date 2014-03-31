package models

/**
 * Ethan Petuchowski
 * 3/30/14
 */

import akka.actor.{ActorRef, Props, Actor}
import play.api.libs.concurrent.Akka
import play.api.Play.current

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
    games get name match {
      case Some(game) =>
        println(s"The name $name is already taken.")
        None
      case None =>
        val newGame = Akka.system.actorOf(Props(new Game(name)))
        newGame ! Join(user)
        games += name -> newGame
        Some(newGame)
    }
  }

  def getGame(name: String) = games get name
}

/**
 * An instance of the Game
 */
case class Game(name: String) extends Actor {

  // Map of username to score
  var scoreboard = Map[String, Int]()

  def receive = {
    case Join(username) => scoreboard += username -> 0
  }
}

case class Join(username: String)
