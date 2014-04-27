package controllers

import play.api._
import play.api.mvc._
import scala.concurrent.{Await, Future, ExecutionContext}
import ExecutionContext.Implicits.global
import play.api.libs.json.{Json, JsString, JsObject, JsValue}
import play.api.libs.iteratee.{Iteratee, Enumerator}
import play.api.libs.concurrent.Promise
import scala.concurrent.duration._
import models.{Scores, GameApp}
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import scala.collection.mutable
import play.libs.F.None
import scala.None

object Application extends Controller {

  /**
   * homepage, listing of available games
   */
  def index(user: Option[String]) = Action { implicit r =>
    Ok(views.html.index(user, GameApp.gameSet))
  }

  /**
   * Called by the login form in the navbar.
   * Verify credentials, and send user signed-in version of the homepage
   * TODO should check whether the username is already taken
   * TODO the entire password implementation
   */
  def login(user: Option[String], password: Option[String]) = Action { implicit r =>
    // Map to None if "user" is an empty string, now render index only if user.isDefined.
    // map{...}getOrElse{...} is a "standard" Scala way to do this
    //    (src for Play's Redirect works the same way)
    user.filterNot { _.isEmpty } map { _ =>
        Ok(views.html.index(user, GameApp.gameSet))
    } getOrElse {
      // `user` didn't work out, so save an error message to the "Flash scope",
      // Flash scope should not be used in Ajax calls because it is subject to race conditions.
      Redirect(routes.Application.index(None)).flashing("error" -> "Please choose a valid username.")
    }
  }

  /**
   * Render page for a particular game
   * Asynchronous because the GameApp "asks" a Game-Aktor for info
   */
  def game(user: Option[String], name: Option[String]): Action[AnyContent] = Action.async {
    if (user.isDefined && name.isDefined) {
      val oneSecTimeout = Promise.timeout("Oops", 1 second)
      val scoresFuture: Future[Any] = GameApp.scoresForGame(name)
      Future.firstCompletedOf(Seq(scoresFuture, oneSecTimeout)) map {
        case scores: Map[String, Int] => Ok(views.html.game(user, name, scores))
        case t: String => InternalServerError(t)
      }
    } else {
      Future(Redirect(routes.Application.index(user)))
    }
  }

  /**
   * User asked to create a new game instance
   * Asynchronous because the GameApp "asks" a Game-Aktor for info
   * TODO this isn't the correct functionality. It should:
   *  1. Check that the game doesn't already exist
   *  2. Create it
   *  3. More realistically, ask the GameApp to perform the above 2 and return some sort of result
   */
  def create(user: Option[String], name: Option[String]) = Action.async { implicit r =>
    if (user.isDefined && name.isDefined) {
      val oneSecTimeout = Promise.timeout("Oops", 1 second)
      val scoresFuture: Future[Any] = GameApp.scoresForGame(name)
      Future.firstCompletedOf(Seq(scoresFuture, oneSecTimeout)) map {
        case scores: Map[String, Int] =>
          Redirect(routes.Application.index(user))
            .flashing("error" -> "game name already exists")
        case n : None => // Not sure this will work at all
          GameApp.createGame(user.get, name.get)
          Ok(views.html.game(user, name, Map.empty[String, Int]))
        case t: String => InternalServerError(t)
      }
    } else {
      Future {
        Redirect(routes.Application.index(user))
          .flashing("error" -> "invalid request")
      }
    }
  }

  /**
   * User requested to join an existing game
   */
  def join(user: Option[String], name: Option[String]) = Action { implicit r =>
    // TODO get the real (scores: Map) to pass to the game view
    Ok(views.html.game(user, name, scores = Map.empty))
  }

  /**
   * TODO management of the WebSocket
   */                 // TODO use async and *ask* an actor for the info
  def gameSocket(user: String) = WebSocket.using[JsValue] { implicit r =>

    println(s"\n$user sent the following message through the chatSocket:")

    // temporarily: send one item through websocket and leave it open
    val out = Enumerator[JsValue](JsObject(Seq("username" -> JsString(user))))

    // temporarily: just pretty-print any received JSON to the console
    val in = Iteratee.foreach[JsValue] { msg => println(Json.prettyPrint(msg)) }

    (in, out)
  }

  /** TODO serve game.js Asset (or something like that; whatever you're supposed to do) */
  def squareGameJs = Action { implicit r =>
    Redirect(routes.Application.index(None))
  }
}
