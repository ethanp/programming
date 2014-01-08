package controllers

import scala.concurrent._

import play.api._
import play.api.mvc._

import play.api.libs.json._
import play.api.libs.functional.syntax._
import play.api.libs.iteratee.Done

import reactivemongo.api._
import reactivemongo.bson.BSONObjectID
import play.modules.reactivemongo._
import play.modules.reactivemongo.json.collection.JSONCollection

import play.autosource.reactivemongo._

import scala.concurrent.ExecutionContext.Implicits.global
import play.api.Play.current

/**
  * Purest Blob autosource directly storing JsObject
  */
object Persons extends ReactiveMongoAutoSourceController[JsObject] {
  val coll = db.collection[JSONCollection]("persons")
}

/**
  * Blob autosource directly storing JsObject with Json validation
  */
object Persons2 extends ReactiveMongoAutoSourceController[JsObject] {

  val coll = db.collection[JSONCollection]("persons")

  override val reader = __.read[JsObject] keepAnd (
    (__ \ "name").read[String] and
    (__ \ "age").read[Int]
  ).tupled
}

/**
  * Person model
  */
case class Person(name: String, age: Int)
object Person{
  // Json Reads+Writes = Format
  implicit val fmt = Json.format[Person]
}

/**
  * Model autosource storing Person with Json validation based Reads[Person]
  */
object Persons3 extends ReactiveMongoAutoSourceController[Person] {

  val coll = db.collection[JSONCollection]("persons")
}

/**
  * Fake User class to be used to simulate security
  */
case class User(name: String)
object User {
  def find(name: String) = Some(User(name))
}

/**
  * Model autosource storing Person with action wrapping for security
  */
object Persons4 extends ReactiveMongoAutoSourceController[Person] {
  def Authenticated(action: User => EssentialAction): EssentialAction = {
    // Let's define a helper function to retrieve a User
    def getUser(request: RequestHeader): Option[User] = {
      request.session.get("user").flatMap(u => User.find(u))
    }

    // Now let's define the new Action
    EssentialAction { request =>
      getUser(request).map(u => action(u)(request)).getOrElse {
        Done(Unauthorized)
      }
    }
  }

  val coll = db.collection[JSONCollection]("persons")

  override def delete(id: BSONObjectID) = Authenticated { _ =>
    super.delete(id)
  }

  override def get(id: BSONObjectID) = Authenticated { _ =>
    super.get(id)
  }

  def login(name: String) = Action {
    Ok("logged in").withSession("user" -> name)
  }

  def logout = Action {
    Ok("logged out").withNewSession
  }
}