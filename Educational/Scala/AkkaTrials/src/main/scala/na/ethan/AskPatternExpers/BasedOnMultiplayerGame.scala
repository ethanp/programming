package na.ethan.AskPatternExpers

/**
 * Ethan Petuchowski
 * 4/27/14
 */
import scala.concurrent.duration._
import akka.actor.{ActorRef, Actor, Props, ActorSystem}
import akka.pattern.ask
import akka.util.Timeout

object GameApp {
  val system = ActorSystem("MultiplayerGame")
  implicit val timeout = Timeout(5.seconds)
  // Map of title to Game
  // private means it has no public getter
  private var games = Map[String, ActorRef]()

  /** create, but don't join, Game aktor */
  def createGame(name: String): Option[ActorRef] = {
    games.get(name) map {
      _ =>
        println(s"createGame: The name $name is already taken.")
        None
    } getOrElse {
      val newGame = system.actorOf(Props(classOf[Game], name))
      games += name -> newGame
      Some(newGame)
    }
  }

  // We'll use this utility method to talk with our Actor
  def sendToGame(name: String, msg: String) {
    println("Me: " + msg)
    games.get(name).get ? msg
    Thread.sleep(100)
  }
  // And our driver
  def main(args: Array[String]) {
    val gameName = "a game"
    createGame(gameName)
    sendToGame(gameName, "Good Morning")
    sendToGame(gameName, "You're terrible")
    system.shutdown()
  }
}

class Game(name: String) extends Actor { // The 'Business Logic'
  def receive = {
    case "Good Morning" =>
      println("Him: Forsooth 'tis the 'morn, but mourneth for thou doest I do!")
    case "You're terrible" => println("Him: Yup")
  }
}
