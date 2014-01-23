package na.ethan.CommentCollector

/**
 * Ethan Petuchowski
 * 1/19/14
 *
 * Reference: https://developers.google.com/youtube/2.0/developers_guide_java
 */

import com.google.gdata.client.youtube.{YouTubeQuery, YouTubeService}
import com.google.gdata.data.geo.impl.GeoRssWhere
import com.google.gdata.data.media.mediarss.MediaThumbnail
import com.google.gdata.data.youtube._


// Java/Scala library
import scala.collection.JavaConverters._
import java.net.URL

object SimpleYoutubeCommentPrinter extends App {

  /** To perform any write or upload operations using the YouTube API,
   *  you need to instantiate a YouTubeService object with your clientID
   *  and developer key. I don't want to do that, but I'll put them here anyway.
   */
  val lines = scala.io.Source.fromFile("/etc/googleIDKey").mkString.split("\n")
  val service : YouTubeService = new YouTubeService(lines(0), lines(1))
  val sampleVideoID = "ADos_xW4_J0" // <== Intro to Google Data
  val videoEntryUrl = "http://gdata.youtube.com/feeds/api/videos/" + sampleVideoID
  val videoEntry = service.getEntry(new URL(videoEntryUrl), classOf[VideoEntry])
//  printVideoEntry(videoEntry, detailed=true)
  val commentsUrl = videoEntry.getComments.getFeedLink.getHref

  // TODO the rest of this needs to be much cleaner
  val commentFeed = service.getFeed(new URL(commentsUrl), classOf[CommentFeed])
  if (videoEntry.getComments != null) {
    val commentsUrl: String = videoEntry.getComments.getFeedLink.getHref
    println(commentsUrl)

    // Get the comment feed use a new query
    val youtubeQuery = new YouTubeQuery(new URL(commentsUrl))
    youtubeQuery.setMaxResults(50) // this is the max
    youtubeQuery.setStartIndex(1)
    val commentUrlFeed = youtubeQuery.getUrl
    println("commentUrlFeed" + commentUrlFeed)
    val commentFeed = service.getFeed(commentUrlFeed, classOf[CommentFeed])
    val entriesList = commentFeed.getEntries
    commentFeed.getNextLink
    commentFeed.getTotalResults

    /*entriesList.asScala.foreach { c : CommentEntry =>*/
    val c = entriesList.get(0)
    println(c)

//    scala.xml.XML.loadString(c)
    println(c.hasSpamHint) // "true if the comment is marked as spam"
    println(c.getAuthors) // supposedly .get(0).getUri.substring(41) for the author id or something
    println(c.getCategories) // "comment", not very helpful
    println(c.getTotalRating)
    println(c.getContent)
    println(c.getId) // supposedly .substring(47) for the id itself
    println(c.getContributors)
    println(c.getHtmlLink)
    println(c.getLinks)
    println(c.getPlainTextContent)
    println(c.getPublished)
    println(c.getService)
    println(c.getSource)
    println(c.getSummary)
    println(c.getTextContent)
    println(c.getTitle)
//    }
    /*************************** Other Guy's Code
    // The response should contain a url for the next feed, if any, already with an updated start-index.
    for { i <- 0 until commentFeed.getEntries.size
          if commentFeed.getEntries.get(i) != null }
    {
      val authorId = commentFeed.getEntries.get(i).getAuthors.get(0).getUri.substring(41)
      val commentId = commentFeed.getEntries.get(i).getId.substring(47)
      val commentContent = commentFeed.getEntries.get(i).getPlainTextContent
      println(authorId+":\n\t"+commentId+":"+commentContent)
    }
  // TODO Loop through next comment feed call, if more can be expected.
  // Use updated url from the response or set start-index = start-index + max-results.
      ***************************/
  }

  def printVideoEntry(videoEntry: VideoEntry, detailed: Boolean) {
    println("Title: " + videoEntry.getTitle.getPlainText)

    if (videoEntry.isDraft) {
      println("Video is not live")
      val pubState = videoEntry.getPublicationState
      if (pubState.getState == YtPublicationState.State.PROCESSING) {
        println("Video is still being processed.")
      }
      else if (pubState.getState == YtPublicationState.State.REJECTED) {
        print("Video has been rejected because: ")
        println(pubState.getDescription)
        print("For help visit: ")
        println(pubState.getHelpUrl)
      }
      else if (pubState.getState == YtPublicationState.State.FAILED) {
        print("Video failed uploading because: ")
        println(pubState.getDescription)
        print("For help visit: ")
        println(pubState.getHelpUrl)
      }
    }

    if (videoEntry.getEditLink != null) {
      println("Video is editable by current user.")
    }

    if (detailed) {

      val mediaGroup = videoEntry.getMediaGroup

      println("Uploaded by: " + mediaGroup.getUploader)
      println("Video ID: " + mediaGroup.getVideoId)
      println("Description: " + mediaGroup.getDescription.getPlainTextContent)

      val mediaPlayer = mediaGroup.getPlayer
      println("Web Player URL: " + mediaPlayer.getUrl)
      val keywords = mediaGroup.getKeywords()
      print("Keywords: ")
      for (keyword: String <- keywords.getKeywords.asScala) {
        print(keyword + ",")
      }

      val location: GeoRssWhere = videoEntry.getGeoCoordinates
      if (location != null) {
        println("Latitude: " + location.getLatitude)
        println("Longitude: " + location.getLongitude)
      }

      val rating = videoEntry.getRating
      if (rating != null) {
        println("Average rating: " + rating.getAverage)
      }

      val stats: YtStatistics = videoEntry.getStatistics
      if (stats != null) {
        println("View count: " + stats.getViewCount)
      }
      println()

      println("\tThumbnails:")
      for (mediaThumbnail: MediaThumbnail <- mediaGroup.getThumbnails.asScala)
      {
        println("\t\tThumbnail URL: " + mediaThumbnail.getUrl)
        println("\t\tThumbnail Time Index: " + mediaThumbnail.getTime)
        println()
      }

      println("\tMedia:")
      for (mediaContent: YouTubeMediaContent <- mediaGroup.getYouTubeContents.asScala) {
        println("\t\tMedia Location: " + mediaContent.getUrl)
        println("\t\tMedia Type: " + mediaContent.getType)
        println("\t\tDuration: " + mediaContent.getDuration)
        println()
      }

      for (mediaRating: YouTubeMediaRating <- mediaGroup.getYouTubeRatings.asScala)
      {
        println("Video restricted in the following countries: " +
          mediaRating.getCountries.toString)
      }
    }
  }

}
