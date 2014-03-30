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
   * the page for a particular game
   */
  def game(id: Option[Int], user: Option[String]) = Action { implicit r =>
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
