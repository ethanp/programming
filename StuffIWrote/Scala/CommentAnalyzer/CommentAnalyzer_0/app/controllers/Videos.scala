package controllers

import play.api.mvc.{Action, Controller}
import models.Video

/**
 * Ethan Petuchowski
 * 1/11/14
 */

object Videos extends Controller {
  def list = Action { implicit request =>
    val videos = Video.findAll

    /* calls videos/list.scala.html
     * which calls main.scala.html with the content to insert */
    Ok(views.html.videos.list(videos))
  }
}
