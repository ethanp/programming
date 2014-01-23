package controllers

import com.google.gdata.client.youtube.YouTubeService
import com.google.gdata.data.youtube.CommentFeed
import com.google.api.services.plus.Plus
import play.api.mvc.{Action, Controller}

object Application extends Controller {

  def index = Action {
    Redirect(routes.Videos.list())
  }

}
