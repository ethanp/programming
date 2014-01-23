package na.ethan.CommentCollector

/**
 * Ethan Petuchowski
 * 1/21/14
 */

import java.net.URL
import com.google.gdata.data.youtube.VideoEntry
import scala.xml.XML
import com.google.gdata.client.youtube.{YouTubeService, YouTubeQuery}
import scala.collection.mutable
import java.util.Date

object SimpleYoutubeCommentParser extends App {
  val NUM_PAGES = 1
  val COMMENT_STEP_SIZE = 50   // this is the max
  val COMMENT_LIMIT = COMMENT_STEP_SIZE * NUM_PAGES
  val lines = scala.io.Source.fromFile("/etc/googleIDKey").mkString.split("\n")
  val service: YouTubeService = new YouTubeService(lines(0), lines(1))
  val sampleVideoID = "ADos_xW4_J0"  // <== Video: "Intro to Google Data"
  val gangnamID = "9bZkp7q19f0"
  val videoEntryUrl = "http://gdata.youtube.com/feeds/api/videos/" + gangnamID
  val videoEntry = service.getEntry(new URL(videoEntryUrl), classOf[VideoEntry])
  val commentsUrl = videoEntry.getComments.getFeedLink.getHref
  val youtubeQuery = new YouTubeQuery(new URL(commentsUrl))
  youtubeQuery.setMaxResults(COMMENT_STEP_SIZE)
  var startIndex = 1
  var commentsReturned = 0
  val format = new java.text.SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'.000Z'")
  val ctsComsIds = mutable.ListBuffer[(Int,String,String)]()
  val allCommentsAndDates = mutable.ListBuffer[(String,Date)]()
  // Note: the total comments /are/ on each blob as <openSearch:totalResults> with limit 1,000,000
  do {
    youtubeQuery.setStartIndex(startIndex)
    val commentUrlFeed = youtubeQuery.getUrl
    println("commentUrlFeed:\t" + commentUrlFeed)
    val entries = XML.load(commentUrlFeed.openStream) \\ "entry"
    val comments: Seq[String] = (entries \\ "content").map(_.text)
    val replyCounts = (entries \\ "replyCount").filter(_.prefix == "yt").map(_.text.toInt)
    val ids = (entries \\ "id").map(_.text).map(".*/comments/(.*)".r.findFirstMatchIn(_).get.group(1))
    val dates: Seq[Date] = (entries \\ "published").map(tag => format.parse(tag.text))
    println(dates.size)
    ctsComsIds ++= (replyCounts, comments, ids).zipped.toList.filterNot(_._1 == 0).toList // keep only those replied-to
    allCommentsAndDates ++= comments.zip(dates)
    startIndex += COMMENT_STEP_SIZE
    commentsReturned = entries.size
    println(ctsComsIds.size)
    println(allCommentsAndDates.size)
  } while ((commentsReturned == COMMENT_STEP_SIZE) && (startIndex < COMMENT_LIMIT))

}
