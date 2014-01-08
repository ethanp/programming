package controllers

import play.api._
import play.api.mvc._
import com.google.gdata.client.youtube.YouTubeService

object Application extends Controller {

  def index = Action {
    Ok(views.html.index("Your new application is ready."))
  }

  val lines = scala.io.Source.fromFile("/etc/googleIDKey").mkString.split("\n")
  val service = new YouTubeService(lines(0), lines(1))


}
