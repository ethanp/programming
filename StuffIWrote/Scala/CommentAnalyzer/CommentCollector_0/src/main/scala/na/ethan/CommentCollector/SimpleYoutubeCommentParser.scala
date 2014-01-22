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

object SimpleYoutubeCommentParser extends App {
  val COMMENT_LIMIT = 100
  val COMMENT_STEP_SIZE = 50   // this is the max
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

  val ctsComsIds = mutable.ListBuffer[(Int,String,String)]()
  val allComments = mutable.ListBuffer[String]()
  // Note: the total comments /are/ on each blob as <openSearch:totalResults> with limit 1,000,000
  do {
    youtubeQuery.setStartIndex(startIndex)
    val commentUrlFeed = youtubeQuery.getUrl
    println("commentUrlFeed:\t" + commentUrlFeed)
    val entries = XML.load(commentUrlFeed.openStream) \\ "entry"
    val comments: Seq[String] = (entries \\ "content").map(_.text)
    val replyCounts = (entries \\ "replyCount").filter(_.prefix == "yt").map(_.text.toInt)
    val ids = (entries \\ "id").map(_.text).map(".*/comments/(.*)".r.findFirstMatchIn(_).get.group(1))
    ctsComsIds ++= (replyCounts, comments, ids).zipped.toList.filter(_._1 > 0).toList // remove replies with no comments
    allComments ++= comments
    startIndex += COMMENT_STEP_SIZE
    commentsReturned = entries.size
    println(ctsComsIds.size)
    println(allComments.size)
  } while ((commentsReturned == COMMENT_STEP_SIZE) && (startIndex < COMMENT_LIMIT))

}
