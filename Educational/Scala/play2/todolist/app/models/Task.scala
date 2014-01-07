package models

case class Task(id: Long, label: String)

object Task {

  import anorm._
  import anorm.SqlParser._

  /* given ResultSet row with id & label columns, create a Task value (instance) */
  val task = {
    get[Long]("id") ~
    get[String]("label") map {
      case id~label => Task(id, label)
    }
  }

  import play.api.db._
  import play.api.Play.current

  def all(): List[Task] = DB.withConnection /*auto create and release JDBC connection*/{ implicit c =>
    /*Anorm*/ SQL("select * from task").as(/*parse ResultSet using:*/ task * /*which parses task rows into List[Task]*/)
  }

  def create(label: String) {
    DB.withConnection { implicit c =>
      SQL("insert into task (label) values ({label})").on(
        'label -> label
      ).executeUpdate()
    }
  }

  def delete(id: Long) {
    DB.withConnection { implicit c =>
      SQL("delete from task where id = {id}").on(
        'id -> id
      ).executeUpdate()
    }
  }

}
