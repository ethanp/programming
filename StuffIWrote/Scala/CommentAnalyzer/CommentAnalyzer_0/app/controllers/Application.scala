package controllers

import com.google.gdata.client.youtube.YouTubeService
import play.api.mvc.{Action, Controller}

object Application extends Controller {

  def index = Action {
    Redirect(routes.Videos.list())
  }

  val lines = scala.io.Source.fromFile("/etc/googleIDKey").mkString.split("\n")
  val service = new YouTubeService(lines(0), lines(1))


}
