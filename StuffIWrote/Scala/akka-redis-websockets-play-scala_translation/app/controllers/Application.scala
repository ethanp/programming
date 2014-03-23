package controllers

import play.api._
import play.api.mvc._
import play.api.libs.json.{JsString, JsObject, JsValue}
import scala.concurrent.{ExecutionContext, Future}
import play.api.libs.iteratee.{Done, Input, Enumerator, Iteratee}
import ExecutionContext.Implicits.global

object Application extends Controller {

  def index = Action {
    Ok(views.html.index("Your new application is ready."))
  }

  def chatRoom(username: Option[String]) = Action {
    username match {
      case Some(string) => Ok(views.html.index(string))
      case _ => Redirect(routes.Application.index())
    }
  }

  def chatSocket(username: String) = WebSocket.async[JsValue] { request =>
    println(s"received $username through the chatSocket")

    // this is explained to the best of my knowledge in my "Notes/Play Notes.md"
    Future[(Iteratee[JsValue,_],Enumerator[JsValue])](
      Done[JsValue,Unit]((),Input.EOF),
      Enumerator[JsValue](JsObject(
        Seq("error" -> JsString("a sample error")))
      ).andThen(Enumerator.enumInput(Input.EOF))
    )
  }
}
