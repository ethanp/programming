package models

/**
 * Ethan Petuchowski
 * 1/22/14
 */

import play.api.Play.current
import play.api.db.DB
import java.util.Date
import anorm._
import anorm.SqlParser.{str, get, int}
import com.google.gdata.client.youtube.{YouTubeQuery, YouTubeService}
import java.net.URL
import com.google.gdata.data.youtube.VideoEntry
import scala.xml.XML
import util.{CommentDownloader, SentimentAnalysis}
import anorm.SqlQuery
import scala.Some
import anorm.~

case class Comment(id             : String,
                   text           : String,
                   published      : Option[Date],
                   numReplies     : Int,
                   videos_id      : String,
                   sentimentValue : Option[Double],
                   comments_id    : Option[String],
                   depth          : Int)
{
  def formattedSentimentValue = sentimentValue.get formatted "%.2f"
}

object Comment {

  def getAllForVideoID(videos_id: String): List[Comment] = DB.withConnection {
    implicit connection =>
      SQL(s"SELECT * FROM comments WHERE videos_id='$videos_id' ORDER BY videos_id ASC").as(commentParser *)
  }

  def avgCommentScoreForVideoID(video_id: String): Double = {
    val comments = getAllForVideoID(video_id)
    comments.map(_.sentimentValue.get).sum / comments.length
  }

  // TODO delete existing comments for this video first
  def downloadCommentsFromVideo(id: String) {
    // TODO make the called methods execute in another thread
    CommentDownloader.downloadCommentsFromVideo(id) foreach { c =>
      insert(c)
      if (c.numReplies > 0)
        downloadCommentsFromComment(c)
    }
  }

  def downloadCommentsFromComment(comment: Comment) {
    CommentDownloader.downloadCommentsFromComment(comment) foreach insert
  }

  val commentParser: RowParser[Comment] = {
    str("id") ~ str("text") ~ get[Option[Date]]("published") ~ int("numReplies") ~ str("videos_id") ~
      get[Option[Double]]("sentimentValue") ~ get[Option[String]]("comments_id") ~ int("depth") map {
         case id ~ text ~ published ~ numReplies ~ videos_id ~ sentimentValue ~ comments_id ~ depth =>
      Comment(id,  text,  published,  numReplies,  videos_id,  sentimentValue,  comments_id,  depth)
    }
  }

  def getAll: List[Comment] = DB.withConnection {
    implicit connection =>
      SQL("select * from comments order by videos_id asc").as(commentParser *)
  }

  // TODO get these via reflection
  val columnList = List("id", "text", "published", "numReplies", "videos_id", "sentimentValue", "comments_id", "depth")

  def commentStringMap(comment: Comment): List[(Any, ParameterValue[_])] = List(
    "id"              -> comment.id,
    "text"            -> comment.text,
    "published"       -> comment.published,
    "numReplies"      -> comment.numReplies,
    "videos_id"       -> comment.videos_id,
    "sentimentValue"  -> comment.sentimentValue,
    "comments_id"     -> comment.comments_id,
    "depth"           -> comment.depth
  )

  def insert(comment: Comment): Boolean = DB.withConnection {
    implicit connection =>
      val addedRows = SQL(fullInsertString).on(
          commentStringMap(comment):_*
        ).executeUpdate()
      addedRows == 1
  }

  def fullInsertString = "insert into comments values " + columnList.map(col => s"{$col}").mkString("(",", ",")")
  def fullUpdateString = "update comments set " + columnList.map(col => s"$col = {$col}").mkString(", ")

  def update(comment: Comment): Boolean = DB.withConnection {
    implicit connection =>
      val updatedRows = SQL(fullUpdateString).on(
        commentStringMap(comment):_*
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
