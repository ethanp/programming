package models

/**
 * Ethan Petuchowski
 * 1/22/14
 */

import play.api.Play.current
import play.api.db.DB
import java.util.Date
import anorm.{ResultSetParser, RowParser, SqlQuery, SQL, ~}
import anorm.SqlParser.{str, get, int}
import com.google.gdata.client.youtube.{YouTubeQuery, YouTubeService}
import java.net.URL
import com.google.gdata.data.youtube.VideoEntry
import scala.xml.XML

case class Comment(id        : String,
                   text      : String,
                   published : Option[Date],
                   numReplies: Int,
                   videos_id : String)

object Comment {
  // TODO optimize by using SQL
  def getAllForVideoID(video_id: String): List[Comment] = {
    val allComments = getAll
    val filteredComments = allComments.filter(_.videos_id == video_id)
    println(allComments.length)
    println(filteredComments.length)
    filteredComments
  }

  def downloadCommentsFromVideo(id: String) {
    // TODO: delete existing comments for this video first
    val NUM_PAGES = 1
    val COMMENT_STEP_SIZE = 50 // this is the max
    val COMMENT_LIMIT = COMMENT_STEP_SIZE * NUM_PAGES
    val lines = scala.io.Source.fromFile("/etc/googleIDKey").mkString.split("\n")
    val service: YouTubeService = new YouTubeService(lines(0), lines(1))
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
      val entries = XML.load(commentUrlFeed.openStream) \\ "entry"
      val comments: Seq[String] = (entries \\ "content").map(_.text)
      val replyCounts = (entries \\ "replyCount").filter(_.prefix == "yt").map(_.text.toInt)
      val ids = (entries \\ "id").map(_.text).map(".*/comments/(.*)".r.findFirstMatchIn(_).get.group(1))
      val dates = (entries \\ "published").map(tag => format.parse(tag.text))
      assert(entries.length == comments.length &&
             entries.length == replyCounts.length &&
             entries.length == ids.length &&
             entries.length == dates.length,
        s"${entries.length}, ${comments.length}, ${replyCounts.length}, ${ids.length}, ${dates.length}, all must match")
      for (i <- 0 until dates.length)
        insert(Comment(ids(i), comments(i), Some(dates(i)), replyCounts(i), id))

      println(dates.size + " comments added to " + id)
      startIndex += COMMENT_STEP_SIZE
      commentsReturned = entries.size
    } while ((commentsReturned == COMMENT_STEP_SIZE) && (startIndex < COMMENT_LIMIT))

  }

  val sql: SqlQuery = SQL("select * from comments order by videos_id asc")

  val commentParser: RowParser[Comment] = {
    str("id") ~ str("text") ~ get[Option[Date]]("published") ~ int("numReplies") ~ str("videos_id") map {
         case id ~ text ~ published ~ numReplies ~ videos_id =>
      Comment(id,  text,  published,  numReplies,  videos_id)
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
          "id"         -> comment.id,
          "text"      -> comment.text,
          "published"  -> comment.published,
          "numReplies" -> comment.numReplies,
          "videos_id"  -> comment.videos_id  /* after I get this working, maybe this can be moved into a def */
        ).executeUpdate()
      addedRows == 1
  }

  def update(comment: Comment): Boolean = DB.withConnection {
    implicit connection =>
      val updatedRows = SQL("update comments set id = {id}, text = {text}, published = {published}, " +
                            "numReplies = {numReplies}, videos_id = {videos_id}").on(
        "id"         -> comment.id,
        "title"      -> comment.text,
        "published"  -> comment.published,
        "numReplies" -> comment.numReplies,
        "videos_id"  -> comment.videos_id
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
