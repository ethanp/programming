package models

/**
 * Ethan Petuchowski
 * 1/23/14
 */

import java.util.Date
import anorm.{ResultSetParser, RowParser, SqlQuery, SQL, ~}
import anorm.SqlParser.{str, get, int}
import play.api.db.DB
import play.api.Play.current

case class CommentReply(id        : String,
                        text      : String,
                        published : Option[Date],
                        numReplies: Int,
                        depth     : Int,
                        comments_id: String)

object CommentReply {
  def retrieveCommentsFromComment(id: String) {}

  // TODO finish converting this from the Comment class

  val sql: SqlQuery = SQL("select * from comment_replies order by comments_id asc")

  val commentReplyParser: RowParser[CommentReply] = {
    str("id")~str("text")~get[Option[Date]]("published")~
      int("numReplies")~int("depth")~str("comments_id") map {
             case  id ~ text ~ published ~ numReplies ~ depth ~ comments_id =>
      CommentReply(id,  text,  published,  numReplies,  depth,  comments_id)
    }
  }

  val commentRepliesParser: ResultSetParser[List[CommentReply]] = commentReplyParser *

  def getAll: List[CommentReply] = DB.withConnection {
    implicit connection =>
      sql.as(commentRepliesParser)
  }

  // Insert, Update, Delete; copied from Video

  def insert(comment: CommentReply): Boolean = DB.withConnection {
    implicit connection =>
      val addedRows = SQL(
        "insert into comments values ({id}, {text}, {published}, {numReplies}, {comments_id})").on(
          "id" -> comment.id,
          "title" -> comment.text,
          "published" -> comment.published,
          "numReplies" -> comment.numReplies,
          "comments_id" -> comment.comments_id /* after I get this working, maybe this can be moved into a val */
        ).executeUpdate()
      addedRows == 1
  }

  def update(comment: CommentReply): Boolean = DB.withConnection {
    implicit connection =>
      val updatedRows = SQL("update comments set id = {id}, text = {text}, published = {published}, " +
        "numReplies = {numReplies}, comments_id = {comments_id}").on(
          "id" -> comment.id,
          "title" -> comment.text,
          "published" -> comment.published,
          "numReplies" -> comment.numReplies,
          "comments_id" -> comment.comments_id
        ).executeUpdate()
      updatedRows == 1
  }

  def delete(comment: CommentReply): Boolean = DB.withConnection {
    implicit connection =>
      val updatedRows = SQL("delete from comments where id = {id}").on(
        "id" -> comment.id
      ).executeUpdate()
      updatedRows == 0
  }
}
