package controllers

import com.google.gdata.client.youtube.YouTubeService
import com.google.gdata.data.youtube.CommentFeed
import com.google.api.services.plus.Plus
import play.api.mvc.{Action, Controller}

object Application extends Controller {

  def index = Action {
    Redirect(routes.Videos.list())
  }

  val lines = scala.io.Source.fromFile("/etc/googleIDKey").mkString.split("\n")
  val service = new YouTubeService(lines(0), lines(1))

  // ehh?? Nobody said to use this, I just say it in the docs...
  // https://developers.google.com/gdata/javadoc/com/google/gdata/data/BaseFeed
  val cf = new CommentFeed()
  cf.getEntries

  // It seems like this is what I have to do
  val listComments = plus.comments.list()

}
