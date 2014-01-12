package controllers

import play.api.mvc.{Action, Controller}
import models.Video

/**
 * Ethan Petuchowski
 * 1/11/14
 *
 * Handles HTTP requests (via 'conf/routes')
 * specific to the Video model
 * and generates responses for views
 */

object Videos extends Controller {
  def list = Action { implicit request =>
    val videos = Video.findAll
    Ok(views.html.videos.list(videos))
  }

  def show(id: Long) = Action { implicit request =>
    Video.findByID(id).map { video =>
      Ok(views.html.videos.details(video))
    }.getOrElse(NotFound)
  }
}
