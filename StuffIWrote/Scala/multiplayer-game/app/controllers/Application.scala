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

// for Forms
import play.api.data._
import play.api.data.Forms._

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
    implicit val us = user
    // Map to None if "user" is an empty string, now render index only if user.isDefined.
    // map{...}getOrElse{...} is a "standard" Scala way to do this
    //    (src for Play's Redirect works the same way)
    user.filterNot { _.isEmpty } map { _ =>
        Ok(views.html.index(user, GameApp.gameSet))
    } getOrElse {
      // `user` didn't work out, so save an error message to the "Flash scope",
      // Flash scope should not be used in Ajax calls because it is subject to race conditions.
      ErrorMsg.invalidUsername
    }
  }

  /**
   * Render page for a particular game
   * Asynchronous because the GameApp "asks" a Game-Aktor for info
   */
  def game(user: Option[String], name: Option[String]): Action[AnyContent] = Action.async { implicit r =>
    implicit val us = user
    if (user.isDefined && name.isDefined) {
      val oneSecTimeout = Promise.timeout("Oops", 1.second)
      val scoresFuture = GameApp.scoresForGame(name.get)
      Future.firstCompletedOf(Seq(scoresFuture, oneSecTimeout)) map {
        case scores: Map[String, Int] => Ok(views.html.game(user, name, scores))
        case t: String => InternalServerError(t)
        case _ => ErrorMsg.gameDoesntExist(user, name)
      }
    } else {
      Future(ErrorMsg.invalidRequest)
    }
  }

  case class CreateGameForm(user: String, name: String)
  val gameForm = Form(mapping("user" -> text,"name" -> text)(CreateGameForm.apply)(CreateGameForm.unapply))

  /**
   * User asked to create a new game instance
   * Asynchronous because the GameApp "asks" a Game-Aktor for a new instance
   */
  def create() = Action.async { implicit r =>
    val form = gameForm.bindFromRequest.get
    if (form.user.length > 0 && form.name.length > 0) {
      val oneSecTimeout = Promise.timeout("Oops", 1.second)
      val scoresFuture = GameApp.scoresForGame(form.name)
      Future.firstCompletedOf(Seq(scoresFuture, oneSecTimeout)) map {

        case scores: Map[String, Int] => ErrorMsg.nameAlreadyExists(Some(form.user))
        case t: String => InternalServerError(t)

        // game doesn't exist, so create it
        case None =>
          GameApp.createGame(form.name)
          Redirect(routes.Application.game(Some(form.user), Some(form.name)))
      }
    } else {
      Future(ErrorMsg.invalidRequest(Some(form.user)))
    }
  }

  /**
   * User requested to join an existing game
   */
  def join(user: Option[String], name: Option[String]) = Action.async { implicit r =>
    implicit val us = user
    if (user.isDefined && name.isDefined) {
      GameApp.joinGame(user.get, name.get) map {
        case scores : Map[String, Int] => Ok(views.html.game(user, name, scores))
        case GameDoesntExist => ErrorMsg.gameDoesntExist(user, name)
        case UsernameAlreadyTaken => ErrorMsg.usernameTaken
      }
    } else {
      Future(ErrorMsg.invalidRequest)
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
  def squareGameJs = Action { implicit r => ??? }

  object ErrorMsg {
    def error(user: Option[String], s: String) = {
      println(s"Error $s, for user $user")
      Redirect(routes.Application.index(user)).flashing("error" -> s)
    }
    def invalidRequest(implicit user: Option[String]) = error(user, "invalid request")
    def usernameTaken(implicit user: Option[String]) = error(user, "username already taken")
    def invalidUsername(implicit user: Option[String]) = error(user, "please choose a valid username")
    def gameDoesntExist(user: Option[String], game: Option[String]) = error(user, s"game $game doesn't exist")
    def nameAlreadyExists(implicit user: Option[String]) = error(user, "game name already exists")
  }
}
