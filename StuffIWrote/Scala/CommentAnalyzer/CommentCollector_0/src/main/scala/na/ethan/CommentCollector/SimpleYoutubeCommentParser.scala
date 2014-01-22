package na.ethan.CommentCollector

/**
 * Ethan Petuchowski
 * 1/21/14
 */

import java.net.URL
import com.google.gdata.data.youtube.{CommentFeed, VideoEntry}
import scala.xml.{Node, XML}
import com.google.gdata.client.youtube.{YouTubeQuery, YouTubeService}
import javax.management.remote.rmi._RMIConnection_Stub

object SimpleYoutubeCommentParser extends App {

  val COMMENT_LIMIT = 50
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

  var ctsComsIds = List()
  // Note: the total comments /are/ on each blob as <openSearch:totalResults> with limit 1,000,000
  do {
    youtubeQuery.setStartIndex(startIndex)
    val commentUrlFeed = youtubeQuery.getUrl
    println("commentUrlFeed:\t" + commentUrlFeed)
    val idRegex = ".*/comments/(.*)".r
    val entries = XML.load(commentUrlFeed.openStream) \\ "entry"
    val comments = (entries \\ "content").map(_.text.toString)
    val replyCounts = (entries \\ "replyCount").filter(_.prefix == "yt").map(_.text.toInt)
    val ids = (entries \\ "id").map(_.text).map {
      idRegex.findFirstMatchIn(_) match {
          case Some(m) => m.group(1)
          case _ => throw new RuntimeException("comment-id not found in comment blob")
      }
    }
    val removeUnreplied = (replyCounts, comments, ids).zipped.toList.filter(_._1 > 0)
    ctsComsIds :::= removeUnreplied.toList
    startIndex += COMMENT_STEP_SIZE
    commentsReturned = service.getFeed(commentUrlFeed, classOf[CommentFeed]).getEntries.size
    println(commentsReturned)
  } while ((commentsReturned == COMMENT_STEP_SIZE) && (startIndex < COMMENT_LIMIT))


}
