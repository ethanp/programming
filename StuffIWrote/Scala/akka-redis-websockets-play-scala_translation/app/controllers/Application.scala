package controllers

import play.api._
import play.api.mvc._

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
}
