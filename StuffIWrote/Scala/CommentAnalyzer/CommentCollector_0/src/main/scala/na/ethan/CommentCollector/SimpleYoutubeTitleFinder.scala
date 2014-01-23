package na.ethan.CommentCollector

/**     Ethan Petuchowski    1/22/14    */

import com.google.gdata.client.youtube.YouTubeService
import java.net.URL
import com.google.gdata.data.youtube.VideoEntry

object SimpleYoutubeTitleFinder extends App {

  val lines = scala.io.Source.fromFile("/etc/googleIDKey").mkString.split("\n")
  val service: YouTubeService = new YouTubeService(lines(0), lines(1))
  val sampleVideoID = "ADos_xW4_J0" // <== "Intro to Google Data...etc."
  val videoEntryUrl = "http://gdata.youtube.com/feeds/api/videos/" + sampleVideoID
  val videoEntry = service.getEntry(new URL(videoEntryUrl), classOf[VideoEntry])
  val title = videoEntry.getTitle.getPlainText
  println(title)

}
