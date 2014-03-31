package controllers

import play.api._
import play.api.mvc._
import scala.concurrent.{Future, ExecutionContext}
import ExecutionContext.Implicits.global
import play.api.libs.json.{Json, JsString, JsObject, JsValue}
import play.api.libs.iteratee.{Iteratee, Enumerator}

object Application extends Controller {

  /**
   * homepage, listing of available games
   */
  def index(user: Option[String]) = Action { implicit r =>
    Ok(views.html.index(user))
  }

  /**
   * Called by the login form in the navbar
   * Should really be replaced with use of the WebSocket
   * Should also check whether the username is already taken
   */
  def login(user: Option[String], password: Option[String]) = Action { implicit r =>
    user.filterNot(_.isEmpty).map { user =>
      Ok(views.html.index(Some(user)))
    }.getOrElse {
      Redirect(routes.Application.index(None)).flashing(
        "error" -> "Please choose a valid username."
      )
    }
  }

  /**
   * the page for a particular game
   */
  def game(user: Option[String], name: Option[String]) = Action { implicit r =>
    Ok(views.html.game(user))
  }

  /**
   * User asked to create a new game instance
   * It's a PUT because it should be idempotent
   */
  def create(user: Option[String], name: Option[String]) = Action { implicit r =>
    Ok(views.html.game(user))
  }

  /**
   * User requested to join an existing game
   */
  def join(user: Option[String], name: Option[String]) = Action { implicit r =>
    Ok(views.html.game(user))
  }

  /**
   * management of the WebSocket
   */                 // TODO use async and *ask* an actor for the info
  def gameSocket(user: String) = WebSocket.using[JsValue] { implicit r =>

    println(s"\n$user sent the following message through the chatSocket:")

    // temporarily: send one item through websocket and leave it open
    val out = Enumerator[JsValue](JsObject(Seq("username" -> JsString(user))))

    // temporarily: just pretty-print any received JSON to the console
    val in = Iteratee.foreach[JsValue] { msg => println(Json.prettyPrint(msg)) }

    (in, out)
  }

  def squareGameJs = Action { implicit r =>
    Redirect(routes.Application.index(None))
  }
}
