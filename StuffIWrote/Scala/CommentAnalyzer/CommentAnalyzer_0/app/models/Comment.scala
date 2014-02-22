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
import util.SentimentAnalysis
import anorm.SqlQuery
import scala.Some
import anorm.~

case class Comment(id             : String,
                   text           : String,
                   published      : Option[Date],
                   numReplies     : Int,
                   videos_id      : String,
                   sentimentValue : Option[Double], // TODO evolve to NOT NULL
                   comments_id    : Option[String],
                   depth          : Option[Int])  // TODO evolve to NOT NULL
{
  def formatted = "%.2f".format(sentimentValue.get)
}

object Comment {

  // TODO optimize by using SQL
  def getAllForVideoID(video_id: String): List[Comment] = getAll.filter(_.videos_id == video_id)

  def avgCommentScoreForVideoID(video_id: String): Double = {
    val comments = getAllForVideoID(video_id)
    comments.map(_.sentimentValue.get).sum / comments.length
  }

  def downloadCommentsFromVideo(id: String) {
    // TODO: delete existing comments for this video first
    val NUM_PAGES = 1
    val COMMENT_STEP_SIZE = 50 // 50 is the max
    val COMMENT_LIMIT = COMMENT_STEP_SIZE * NUM_PAGES
    val creds = scala.io.Source.fromFile("/etc/googleIDKey").mkString.split("\n")
    val service = new YouTubeService(creds(0), creds(1))
    val videoEntryUrl = "http://gdata.youtube.com/feeds/api/videos/" + id
    val videoEntry = service.getEntry(new URL(videoEntryUrl), classOf[VideoEntry])
    val commentsUrl = videoEntry.getComments.getFeedLink.getHref
    val youtubeQuery = new YouTubeQuery(new URL(commentsUrl))
    youtubeQuery.setMaxResults(COMMENT_STEP_SIZE)
    var startIndex = 1
    var commentsReturned = 0
    val format = new java.text.SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'.000Z'")
    do {
      youtubeQuery.setStartIndex(startIndex)
      val commentUrlFeed = youtubeQuery.getUrl
      println("commentUrlFeed:\t" + commentUrlFeed)

      // list of 'entry' blobs from the xml returned by the comment-feed's url
      val entries = XML.load(commentUrlFeed.openStream) \\ "entry"

      // extract the comments
      val comments: Seq[String] = (entries \\ "content").map(_.text)
      val sentimentVals = comments.map(c => SentimentAnalysis.analyzeText(c))
      val replyCounts = (entries \\ "replyCount").filter(_.prefix == "yt").map(_.text.toInt)
      val ids = (entries \\ "id").map(_.text).map("/comments/(.*)".r.findFirstMatchIn(_).get.group(1))
      val dates = (entries \\ "published").map(tag => format.parse(tag.text))
      assert(entries.length == comments.length
          && entries.length == replyCounts.length
          && entries.length == ids.length
          && entries.length == dates.length,
        s"${entries.length}, ${comments.length}, ${replyCounts.length}, ${ids.length}, ${dates.length}, all must match")

      for (i <- 0 until dates.length)
        insert(Comment(ids(i), comments(i), Some(dates(i)), replyCounts(i), id, Some(sentimentVals(i)), null, Some(0)))

      println(dates.size + " comments added to " + id)
      startIndex += COMMENT_STEP_SIZE
      commentsReturned = entries.size
    } while ((commentsReturned == COMMENT_STEP_SIZE) && (startIndex < COMMENT_LIMIT))
  }

  val commentParser: RowParser[Comment] = {
    str("id") ~ str("text") ~ get[Option[Date]]("published") ~ int("numReplies") ~ str("videos_id") ~
      get[Option[Double]]("sentimentValue") ~ get[Option[String]]("comments_id") ~ get[Option[Int]]("depth") map {
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
