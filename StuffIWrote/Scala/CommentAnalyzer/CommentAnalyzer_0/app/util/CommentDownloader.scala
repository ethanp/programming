package util

import com.google.gdata.client.youtube.{YouTubeQuery, YouTubeService}
import java.net.URL
import com.google.gdata.data.youtube.VideoEntry
import scala.xml.XML
import java.io.{IOException, FileInputStream, InputStreamReader, File}
import com.google.api.client.util.store.FileDataStoreFactory
import com.google.api.client.json.jackson2.JacksonFactory
import com.google.api.client.http.HttpTransport
import com.google.api.services.plus.{PlusScopes, Plus}
import com.google.api.client.auth.oauth2.Credential
import com.google.api.client.googleapis.auth.oauth2.{GoogleAuthorizationCodeFlow, GoogleClientSecrets}
import java.util.{Date, HashSet, Set}
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport
import com.google.api.services.plus.model.{Comment => gComment}
import models.{Comment => mComment}
import scala.collection.JavaConverters._


/**
 * Ethan Petuchowski
 * 2/22/14
 */
object CommentDownloader {

  /** connection code translated from plus-cmd-line-sample:PlusSample.java */

  /** best practice is to use these as globally shared instances across app */
  val APPLICATION_NAME = "ethanp-CommentCollector/0.0"
  /** dir to store user credentials */
  val DATA_STORE_DIR = new File(System.getProperty("user.home"), ".store/CommColl")
  var dataStoreFactory: FileDataStoreFactory = _
  val JSON_FACTORY = JacksonFactory.getDefaultInstance

  var httpTransport: HttpTransport = _
  var client: Plus = _

  val googleDateFormat = new java.text.SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'.000Z'")

  def downloadCommentsFromVideo(id: String, NUM_PAGES : Int = 1, COMMENT_STEP_SIZE : Int = 50):
  List[mComment] = {
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
    var commentsList = List[mComment]()
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
      val dates = entries \\ "published" map (tag => googleDateFormat parse tag.text)
      assert(entries.length == comments.length
          && entries.length == replyCounts.length
          && entries.length == ids.length
          && entries.length == dates.length,
        s"${entries.length}, ${comments.length}, ${replyCounts.length}, ${ids.length}, ${dates.length}, all must match")

      for (i <- 0 until dates.length)
        commentsList ::= mComment(ids(i), comments(i), Some(dates(i)), replyCounts(i), id, Some(sentimentVals(i)), null, 0)

      println(dates.size + " comments added to " + id)
      startIndex += COMMENT_STEP_SIZE
      commentsReturned = entries.size
    } while ((commentsReturned == COMMENT_STEP_SIZE) && (startIndex < COMMENT_LIMIT))
    commentsList
  }

  @throws[Exception]
  private def authorize(): Credential = {
    // load client secrets
    val clientSecrets = GoogleClientSecrets.load(JSON_FACTORY,
      new InputStreamReader(new FileInputStream("/etc/google_keys/client_secrets.json")))

    if (clientSecrets.getDetails.getClientId.startsWith("Enter")
        || clientSecrets.getDetails.getClientSecret.startsWith("Enter")) {
      System.out.println("Download client_secrets.json from the Console's Quickstart tool")
      System.exit(1)
    }

    // Set up authorization code flow.
    // Remove scopes that you are not actually using.
    val scopes: Set[String] = new HashSet[String]
    scopes.add(PlusScopes.PLUS_LOGIN)
//    scopes.add(PlusScopes.PLUS_ME)
//    scopes.add(PlusScopes.USERINFO_EMAIL)
//    scopes.add(PlusScopes.USERINFO_PROFILE)

    val flow = new GoogleAuthorizationCodeFlow.Builder(
      httpTransport, JSON_FACTORY, clientSecrets, scopes
    ).setDataStoreFactory(dataStoreFactory).build()

    new AuthorizationCodeInstalledApp(flow, new LocalServerReceiver()).authorize("user")
  }

  def downloadCommentsFromComment(basedOn: mComment, MAX_RESULTS: Long = 500L): List[mComment] = {
    try {
      httpTransport = GoogleNetHttpTransport.newTrustedTransport
      dataStoreFactory = new FileDataStoreFactory(DATA_STORE_DIR)
      val credential = authorize
      client = new Plus.Builder(httpTransport, JSON_FACTORY, credential)
        .setApplicationName(APPLICATION_NAME).build()

      /** translated & simplified from
        * https://developers.google.com/+/api/latest/comments/list#examples */

      val activityId = "z12jcvs5spvzzlw2122bgrugssb1dna0e"  // <== comment on Gangnam Style

      // One may also want to take the number of plus-one'rs into account, via: `gComments map (_.getPlusoners.getTotalItems)`

      val listComments = client.comments.list(basedOn.id)
      listComments.setMaxResults(MAX_RESULTS)
      val commentFeed = listComments.execute()
      val gComments : List[gComment] = commentFeed.getItems.asScala.toList

      return gComments.map { c =>
        val text = c.getObject.getContent
        mComment(
          id              = c.getId,
          text            = text,
          published       = Some(new Date(c.getPublished.getValue)),
          numReplies      = 0,
          videos_id       = basedOn.videos_id,
          sentimentValue  = Some(SentimentAnalysis.analyzeText(text)),
          comments_id     = Some(basedOn.id),
          depth           = 1
        )
      }
    }
    catch {
      case e: IOException => System.err.println(e.getMessage)
      case t: Throwable => t.printStackTrace()
    }
    Nil
  }
}
