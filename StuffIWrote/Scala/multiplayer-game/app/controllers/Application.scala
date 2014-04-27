package controllers

import play.api.mvc._
import scala.concurrent.{Future, ExecutionContext}
import ExecutionContext.Implicits.global
import play.api.libs.json.{Json, JsString, JsObject, JsValue}
import play.api.libs.iteratee.{Iteratee, Enumerator}
import play.api.libs.concurrent.Promise
import scala.concurrent.duration._
import models.{UsernameAlreadyTaken, GameDoesntExist, GameApp}
import play.api.libs.concurrent.Execution.Implicits.defaultContext
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
   * We allow the same User to Login on multiple computers, but they can't both Join the same Game
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
      ErrorMsg.invalidUsername(user)
    }
  }

  /**
   * Render page for a particular game
   * Asynchronous because the GameApp "asks" a Game-Aktor for info
   */
  def game(user: Option[String], name: Option[String]): Action[AnyContent] = Action.async { implicit r =>
    if (user.isDefined && name.isDefined) {
      val oneSecTimeout = Promise.timeout("Oops", 1 second)
      val scoresFuture = GameApp.scoresForGame(name.get)
      Future.firstCompletedOf(Seq(scoresFuture, oneSecTimeout)) map {
        case scores: Map[String, Int] => Ok(views.html.game(user, name, scores))
        case t: String => InternalServerError(t)
        case _ => ErrorMsg.gameDoesntExist(user)
      }
    } else {
      Future(ErrorMsg.invalidRequest(user))
    }
  }

  /**
   * User asked to create a new game instance
   * Asynchronous because the GameApp "asks" a Game-Aktor for a new instance
   */
  def create(user: Option[String], name: Option[String]) = Action.async { implicit r =>
    if (user.isDefined && name.isDefined) {
      val oneSecTimeout = Promise.timeout("Oops", 1 second)
      val scoresFuture = GameApp.scoresForGame(name.get)
      Future.firstCompletedOf(Seq(scoresFuture, oneSecTimeout)) map {

        case scores: Map[String, Int] => ErrorMsg.nameAlreadyExists(user)
        case t: String => InternalServerError(t)

        // game doesn't exist, so create it
        case None =>
          GameApp.createGame(user.get, name.get)
          Ok(views.html.game(user, name, Map[String, Int]()))
      }
    } else {
      Future(ErrorMsg.invalidRequest(user))
    }
  }

  /**
   * User requested to join an existing game
   */
  def join(user: Option[String], name: Option[String]) = Action.async { implicit r =>
    if (user.isDefined && name.isDefined) {
      GameApp.joinGame(user.get, name.get) map {
        case scores : Map[String, Int] => Ok(views.html.game(user, name, scores))
        case GameDoesntExist => ErrorMsg.gameDoesntExist(user)
        case UsernameAlreadyTaken => ErrorMsg.usernameTaken(user)
      }
    } else {
      Future(ErrorMsg.invalidRequest(user))
    }
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
  // http://www.playframework.com/documentation/2.2.x/Assets
  def squareGameJs = Action { implicit r =>
    Redirect(routes.Application.index(None))
  }

  object ErrorMsg {
    def error(user: Option[String], s: String) = Redirect(routes.Application.index(user)).flashing("error" -> s)
    def invalidRequest(user: Option[String]) = error(user, "invalid request")
    def usernameTaken(user: Option[String]) = error(user, "username already taken")
    def invalidUsername(user: Option[String]) = error(user, "please choose a valid username")
    def gameDoesntExist(user: Option[String]) = error(user, "game doesn't exist")
    def nameAlreadyExists(user: Option[String]) = error(user, "game name already exists")
  }
}
