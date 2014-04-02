package controllers

import play.api._
import play.api.mvc._
import scala.concurrent.{Await, Future, ExecutionContext}
import ExecutionContext.Implicits.global
import play.api.libs.json.{Json, JsString, JsObject, JsValue}
import play.api.libs.iteratee.{Iteratee, Enumerator}
import scala.concurrent.duration._
import models.{Scores, GameApp}

object Application extends Controller {

  /**
   * homepage, listing of available games
   */
  def index(user: Option[String]) = Action { implicit r =>
    Ok(views.html.index(user, GameApp.gameSet))
  }

  /**
   * Called by the login form in the navbar
   * Should really be replaced with use of the WebSocket
   * Should also check whether the username is already taken
   */
  def login(user: Option[String], password: Option[String]) = Action { implicit r =>
    user.filterNot(_.isEmpty).map { user =>
      Ok(views.html.index(Some(user), GameApp.gameSet))
    }.getOrElse {
      Redirect(routes.Application.index(None)).flashing(
        "error" -> "Please choose a valid username."
      )
    }
  }

  /**
   * the page for a particular game
   */
  def game(user: Option[String], name: Option[String]) = {
    if (name.isDefined) {
      // I don't think this is how you're supposed to do it
      val scoresFuture = GameApp.scoresForGame(name.get)
      Await.result(scoresFuture, 5 seconds)
      scoresFuture onSuccess { case scores: Map[String, Int] =>
        Action { implicit r =>
          Ok(views.html.game(user, name, scores))
        }
      }
      scoresFuture onFailure {
        case e : Exception => println("Exception in Application.game")
      }
    }
    else
      Redirect(routes.Application.index(user))
  }

  /**
   * User asked to create a new game instance
   */
  def create(user: Option[String], name: Option[String]) = {
    if (name.isDefined) {
      // TODO this should be calling GameApp.createGame(name.get) not scoresForGame...
      val scoresFuture = GameApp.scoresForGame(name.get)
      Await.result(scoresFuture, 5 seconds)
      scoresFuture onSuccess {
        case scores: Map[String, Int] =>
          Action {
            implicit r =>
              (user, name) match {
                case (Some(u), Some(n)) =>
                  GameApp.createGame(u, n) map {
                    _ =>
                      Ok(views.html.game(user, name, scores))
                  } getOrElse {
                    // TODO flash a little "Game already exists" or something
                    Redirect(routes.Application.index(user))
                  }
                case _ => Redirect(routes.Application.index(user))
              }
          }
      }
    }
    else
      Redirect(routes.Application.index(user))
  }

  /**
   * User requested to join an existing game
   */
  def join(user: Option[String], name: Option[String]) = {
    // TODO method needs to ask the appropriate Game actor to add this user before it can render the game
    Action {
      implicit r =>
        Ok(views.html.game(user, name))
    }
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
