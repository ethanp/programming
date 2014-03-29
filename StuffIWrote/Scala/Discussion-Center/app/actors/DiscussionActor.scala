package actors

/**
 * Ethan Petuchowski
 * 3/23/14
 *
 * this is my own typing-in of the code in the framework's sample "websocket-chat"
 */

import akka.actor.{ActorRef, Props, Actor}
import akka.util.Timeout
import scala.concurrent.duration._
import play.api.libs.concurrent.Akka

case class Join(username: String)
case class CannotConnect(errorMessage: String)

/**
 * The DiscussionActor manages a particular Discussion within the App.
 */
class DiscussionActor extends Actor {
  var members = Set.empty[String] // empty: "factory method for empty sequences"
  def receive: Receive = {
    case Join(username) => {
      if (members contains username) {
        sender ! CannotConnect("This username is in use")
      } else {
        members += username
      }
    }
  }
}

/**
 * The DiscussionApp handles the boot-up and boot-down of the App.
 * This means loading the Redis DB from disk and putting the
 * data in the appropriate place, and whatever initializations are req'd.
 */
object DiscussionApp {
  implicit val timeout = Timeout(1 second)

  // this would change (probably to a Seq[]) if I implement multiple chatrooms
  val defaultChatRoom = Akka.system.actorOf(Props[DiscussionActor])

  def d = ???
}
