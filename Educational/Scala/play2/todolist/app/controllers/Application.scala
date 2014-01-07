package controllers

import play.api._
import play.api.mvc._


object Application extends Controller {

  import play.api.data._
  import play.api.data.Forms._

  val taskForm = Form(
    "label" -> nonEmptyText /* validate that label is nonempty */
  )


  def index = Action {
    Redirect(routes.Application.tasks)
  }

  import models.Task

  def tasks = Action {
    Ok(views.html.index(Task.all(), taskForm))
  }

  def newTask = Action { implicit request =>
    taskForm.bindFromRequest.fold(
      errors => /*400*/ BadRequest(views.html.index(Task.all(), errors)),
      label => {
        Task.create(label)
        Redirect(routes.Application.tasks)
      }
    )
  }

  def deleteTask(id: Long) = Action {
    Task.delete(id)
    Redirect(routes.Application.tasks)
  }

}
