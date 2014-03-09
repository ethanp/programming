/**
 * Ethan Petuchowski
 * 3/8/14
 *
 * Goal:
 * -----
 * Efficiently download and analyze comments from YouTube
 * by passing them between different actors that do different
 * pieces of the whole pipeline.
 *
 * Problem this Addresses:
 * -----------------------
 * My app currently does the following:
 *
 *  1. Download next page of youtube comments (network)
 *  2. Parse comments into components (CPU)
 *  3. Calculate sentiment value of comments (CPU)
 *  4. Store comments in database (I/O)
 *  5. Repeat as necessary
 *
 * NOTE: Each repetition has to wait on the last, but this is for nought!
 *
 * I'd like to parallelize this by introducing actors which are each
 * responsible for a different piece of the action, but I could have
 * a bunch of them doing performing each task at the same time, with
 * no hard limit as far as the program itself is concerned.
 */

package na.ethan.AkkaRetrieval

import akka.actor._
import com.google.gdata.client.youtube.{YouTubeQuery, YouTubeService}
import java.net.URL
import com.google.gdata.data.youtube.{CommentEntry, CommentFeed, VideoEntry}
import scala.collection.JavaConverters._

case class IDToLookFor(id: String)
case object RetrieveNextPage
case class ParseYouTubeComments(comments: List[CommentEntry])

// TODO create abstract base Retriever class

/**
 * Downloads a given page of comments from youtube
 */
class YouTubeRetriever extends Actor {
    var video_id = ""
    var page_num = 0
    val parser = context.actorOf(Props[YouTubeCommentParser], "YouTubeCommentParser")
    val lines = scala.io.Source.fromFile("/etc/googleIDKey").mkString.split("\n")
    val service : YouTubeService = new YouTubeService(lines(0), lines(1))

    def getComments = {
        val videoEntryUrl = "http://gdata.youtube.com/feeds/api/videos/" + video_id
        val videoEntry = service.getEntry(new URL(videoEntryUrl), classOf[VideoEntry])
        val comments = videoEntry.getComments
        if (comments != null) {
            val commentsUrl: String = comments.getFeedLink.getHref
            println(commentsUrl)

            // Get the comment feed use a new query
            val youtubeQuery = new YouTubeQuery(new URL(commentsUrl))
            val RESULTS_PER_PAGE = 50 // this is the max
            youtubeQuery.setMaxResults(RESULTS_PER_PAGE)
            youtubeQuery.setStartIndex(page_num * RESULTS_PER_PAGE + 1) // TODO this doesn't seem to work
            val commentUrlFeed = youtubeQuery.getUrl
            println("commentUrlFeed" + commentUrlFeed)
            val commentFeed = service.getFeed(commentUrlFeed, classOf[CommentFeed])
            val entriesList = commentFeed.getEntries.asScala.toList

            commentFeed.getNextLink      // TODO use this to download the next time
            commentFeed.getTotalResults  // TODO use this to decide whether there are more available

            /*entriesList.asScala.foreach { c : CommentEntry =>*/
            parser ! ParseYouTubeComments(entriesList)
        }
    }

    def receive: Actor.Receive = {
        case IDToLookFor(id) => {
            println(s"looking for: $id")
            video_id = id
            page_num = 0
        }
        case RetrieveNextPage => {
            println(s"retrieving page: $page_num")
            getComments
            page_num += 1
        }
    }
}

/**
 * Downloads a given page of comments from Google Plus
 */
class GPlusRetriever extends Actor {
    var gplus_id = ""
    var page_num = 0
    val parser = context.actorOf(Props[GPlusCommentParser], "GPlusCommentParser")

    def receive: Actor.Receive = {
        case IDToLookFor(id) => {
            println(s"looking for: $id")
            gplus_id = id
            page_num = 0
        }
        case RetrieveNextPage => {
            println(s"retrieving page: $page_num")
            page_num += 1
        }
    }
}

/**
 * Turns a page of comments into a list of comment components
 */
class YouTubeCommentParser extends Actor {
    def receive = {
        case ParseYouTubeComments(comments) => {
            comments.foreach(c => println(c.getPlainTextContent))
        }
    }
}

/**
 * Turns a page of comments into a list of comment components
 */
class GPlusCommentParser extends Actor {
    def receive = {
        case _ => ???
    }
}

/**
 * Returns the sentiment value of a given piece of text
 */
class SentimentAnalyzer extends Actor {
    def receive = {
        case _ => ???
    }
}

object HelloGDataWithAkka extends App {
    println("First Line")
    val system = ActorSystem(name="helloGData")
    val retriever : ActorRef = system.actorOf(props=Props[YouTubeRetriever], name="retriever")
    val inbox = Inbox.create(system)
    retriever ! IDToLookFor("ADos_xW4_J0")  // <== "Intro to Google Data...etc."
    retriever ! RetrieveNextPage
    retriever ! RetrieveNextPage
    println("Final Line")
}
