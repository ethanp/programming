package models

/**
 * Ethan Petuchowski
 * 1/22/14
 */

import play.api.Play.current
import play.api.db.DB
import java.util.Date
import anorm.{ResultSetParser, RowParser, SqlQuery, SQL, ~}
import anorm.SqlParser.{str, date, int}

case class Comment(id: String, text: String, published: Date, numReplies: Int, videos_id: String)

object Comment {
  val sql: SqlQuery = SQL("select * from comments order by videos_id asc")

  val commentParser: RowParser[Comment] = {
    str("id")~str("text")~date("published")~int("numReplies")~str("videos_id") map {
      case id~text~published~numReplies~videos_id => Comment(id,text,published,numReplies,videos_id)
    }
  }

  val commentsParser: ResultSetParser[List[Comment]] = commentParser *

  def getAll: List[Comment] = DB.withConnection {
    implicit connection =>
      sql.as(commentsParser)
  }

  // Insert, Update, Delete; copied from Video

  def insert(comment: Comment): Boolean = DB.withConnection {
    implicit connection =>
      val addedRows = SQL(
        "insert into comments values ({id}, {text}, {published}, {numReplies}, {videos_id})").on(
          "id" -> comment.id,
          "title" -> comment.text,
          "published" -> comment.published,
          "numReplies" -> comment.numReplies,
          "videos_id" -> comment.videos_id  /* after I get this working, maybe this can be moved into a val */
        ).executeUpdate()
      addedRows == 1
  }

  def update(comment: Comment): Boolean = DB.withConnection {
    implicit connection =>
      val updatedRows = SQL("update comments set id = {id}, text = {text}, published = {published}, " +
                            "numReplies = {numReplies}, videos_id = {videos_id}").on(
        "id" -> comment.id,
        "title" -> comment.text,
        "published" -> comment.published,
        "numReplies" -> comment.numReplies,
        "videos_id" -> comment.videos_id
      ).executeUpdate()
      updatedRows == 1
  }

  def delete(comment: Comment): Boolean = DB.withConnection {
    implicit connection =>
      val updatedRows = SQL("delete from comments where id = {id}").on(
        "id" -> comment.id
      ).executeUpdate()
      updatedRows == 0
  }
}
