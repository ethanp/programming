package util

import com.google.gdata.client.youtube.{YouTubeQuery, YouTubeService}
import java.net.URL
import com.google.gdata.data.youtube.VideoEntry
import scala.xml.XML
import models.Comment

/**
 * Ethan Petuchowski
 * 2/22/14
 */
object CommentDownloader {
  def downloadCommentsFromVideo(id: String, NUM_PAGES : Int = 1, COMMENT_STEP_SIZE : Int = 50):
  List[Comment] = {
    val COMMENT_LIMIT = COMMENT_STEP_SIZE * NUM_PAGES
    val creds = scala.io.Source.fromFile("/etc/googleIDKey").mkString split "\n"
    val service = new YouTubeService(creds(0), creds(1))
    val videoEntryUrl = "http://gdata.youtube.com/feeds/api/videos/" + id
    val videoEntry = service.getEntry(new URL(videoEntryUrl), classOf[VideoEntry])
    val commentsUrl = videoEntry.getComments.getFeedLink.getHref
    val youtubeQuery = new YouTubeQuery(new URL(commentsUrl))
    youtubeQuery setMaxResults COMMENT_STEP_SIZE
    var startIndex = 1
    var commentsReturned = 0
    val format = new java.text.SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'.000Z'")
    var commentsList = List[Comment]()
    do {
      youtubeQuery setStartIndex startIndex
      val commentUrlFeed = youtubeQuery.getUrl
      println("commentUrlFeed:\t" + commentUrlFeed)

      // list of 'entry' blobs from the xml returned by the comment-feed's url
      val entries = (XML load commentUrlFeed.openStream) \\ "entry"

      // extract the comments
      val comments: Seq[String] = entries \\ "content" map (_.text)
      val sentimentVals = comments map SentimentAnalysis.analyzeText
      val replyCounts = entries \\ "replyCount" collect { case count if count.prefix == "yt" => count.text.toInt }
      val ids = entries \\ "id" map (n => "/comments/(.*)".r.findFirstMatchIn(n.text).get.group(1))
      val dates = entries \\ "published" map (tag => format parse tag.text)
      assert(entries.length == comments.length
          && entries.length == replyCounts.length
          && entries.length == ids.length
          && entries.length == dates.length,
        s"${entries.length}, ${comments.length}, ${replyCounts.length}, ${ids.length}, ${dates.length}, all must match")

      for (i <- 0 until dates.length)
        commentsList ::= Comment(ids(i), comments(i), Some(dates(i)), replyCounts(i), id, Some(sentimentVals(i)), null, 0)

      println(dates.size + " comments added to " + id)
      startIndex += COMMENT_STEP_SIZE
      commentsReturned = entries.size
    } while ((commentsReturned == COMMENT_STEP_SIZE) && (startIndex < COMMENT_LIMIT))
    commentsList
  }

  def downloadCommentsFromComment(id: String): List[Comment] = {
    Nil
  }
}
