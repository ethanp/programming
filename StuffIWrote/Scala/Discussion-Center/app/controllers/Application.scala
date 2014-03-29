package controllers

import play.api._
import play.api.mvc._
import play.api.libs.json.{JsString, JsObject, JsValue}
import scala.concurrent.{ExecutionContext, Future}
import play.api.libs.iteratee._
import ExecutionContext.Implicits.global
import play.api.libs.concurrent.Akka
import scala.Some

object Application extends Controller {

  def index = Action { implicit request =>
    Ok(views.html.index(Some("This is the landing page")))
  }

  def chatRoom(username: Option[String]) = Action { implicit request =>
    username match {
      case Some(string) => Ok(views.html.index(Some(string)))
      case _ => Redirect(routes.Application.index())
    }
  }

  def chatSocket(username: String) = WebSocket.using[JsValue] { implicit request =>
    println(s"received $username through the chatSocket")

    /* based on comment posted on
     * blog.tksfz.org/2012/10/12/websockets-echo-using-play-scala-and-actors-part-i
     */

    // create Enumerator to send through client socket
//    val (out, channel) = Concurrent.broadcast[JsValue]
    val out = Enumerator[JsValue](JsObject(Seq("username" -> JsString(username))))
    // create Iteratee to receive from client socket
//    val in = Iteratee.foreach[JsValue] { message => channel.push(message) }
    val in = Iteratee.foreach[JsValue] { message => println(message) }

    (in, out)
  }

  def chatRoomJs(username: String) = Action { implicit request =>
    Ok(views.js.chatRoom(username))
  }
}
