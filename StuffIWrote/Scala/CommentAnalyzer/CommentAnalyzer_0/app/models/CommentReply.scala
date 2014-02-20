package models

/**
 * Ethan Petuchowski
 * 1/23/14
 */

import java.util.Date
import anorm._
import anorm.SqlParser.{str, get, int}
import play.api.db.DB
import play.api.Play.current
import anorm.~
import anorm.SqlQuery

case class CommentReply(id          : String,
                        text        : String,
                        published   : Option[Date],
                        numReplies  : Int,
                        depth       : Int,
                        comments_id : String,  // <== this isn't a real FK because depending on the depth,
                        sentimentValue: Option[Double]) // it might be in here, or the Comments table
object CommentReply {                                   // this means there's "on delete or update, cascade"
  def downloadCommentsOnComment(id: String) {

  }

  val sql: SqlQuery = SQL("select * from comment_replies order by comments_id asc")

  val commentReplyParser: RowParser[CommentReply] = {
    str("id")~str("text")~get[Option[Date]]("published")~
      int("numReplies")~int("depth")~str("comments_id") ~ get[Option[Double]]("sentimentValue") map {
             case  id ~ text ~ published ~ numReplies ~ depth ~ comments_id ~ sentimentValue =>
      CommentReply(id,  text,  published,  numReplies,  depth,  comments_id, sentimentValue)
    }
  }

  val commentRepliesParser: ResultSetParser[List[CommentReply]] = commentReplyParser *

  def getAll: List[CommentReply] = DB.withConnection {
    implicit connection =>
      sql.as(commentRepliesParser)
  }

  def commentStringMap(comment: CommentReply): List[(Any, ParameterValue[_])] = List(
          "id"              -> comment.id,
          "title"           -> comment.text,
          "published"       -> comment.published,
          "numReplies"      -> comment.numReplies,
          "depth"           -> comment.depth,
          "comments_id"     -> comment.comments_id,
          "sentimentValue"  -> comment.sentimentValue
  )

  def insert(comment: CommentReply): Boolean = DB.withConnection {
    implicit connection =>
      val addedRows = SQL(
        "insert into comments values ({id}, {text}, {published}, {numReplies}, {depth}, {comments_id}, {sentimentValue})").on(
          commentStringMap(comment):_*
        ).executeUpdate()
      addedRows == 1
  }

  def update(comment: CommentReply): Boolean = DB.withConnection {
    implicit connection =>
      val updatedRows = SQL("update comments set id = {id}, text = {text}, published = {published}, " +
        "numReplies = {numReplies}, comments_id = {comments_id}").on(
          commentStringMap(comment):_*
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
