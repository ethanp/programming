package na.ethan.CommentCollector

/**
 * Ethan Petuchowski
 * 1/19/14
 */

// authorization
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver
import com.google.api.client.googleapis.auth.oauth2.{GoogleAuthorizationCodeFlow, GoogleClientSecrets}
import com.google.api.client.auth.oauth2.Credential

// transport and de/serialization
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport
import com.google.api.client.util.store.FileDataStoreFactory
import com.google.api.client.json.jackson2.JacksonFactory
import com.google.api.client.http.HttpTransport

// G+ models
import com.google.api.services.plus.{Plus, PlusScopes}
import com.google.api.services.plus.model.Comment

// java/scala libraries
import java.io.{FileInputStream, File, IOException, InputStreamReader}
import java.util.HashSet
import java.util.Set
import scala.collection.JavaConverters._

object SimpleCommentResponsePrinter {

  /** connection code translated from plus-cmd-line-sample:PlusSample.java */

  /** best practice is to use these as globally shared instances across app */
  private val APPLICATION_NAME = "ethanp-CommentCollector/0.0"
  /** dir to store user credentials */
  private val DATA_STORE_DIR = new File(System.getProperty("user.home"), ".store/CommColl")
  private var dataStoreFactory: FileDataStoreFactory = _
  private val JSON_FACTORY = JacksonFactory.getDefaultInstance
  private var httpTransport: HttpTransport = _
  private var client: Plus = _

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

  def main(args: Array[String]) {
    try {
      httpTransport = GoogleNetHttpTransport.newTrustedTransport
      dataStoreFactory = new FileDataStoreFactory(DATA_STORE_DIR)
      val credential = authorize
      client = new Plus.Builder(httpTransport, JSON_FACTORY, credential)
        .setApplicationName(APPLICATION_NAME).build()

      /** translated & simplified from
        * https://developers.google.com/+/api/latest/comments/list#examples */
      val activityId = "z12jcvs5spvzzlw2122bgrugssb1dna0e"  // <== comment on Gangnam Style
      val listComments = client.comments.list(activityId)
      listComments.setMaxResults(500) // it looks like this might be the max
      val commentFeed = listComments.execute()
      val comments : List[Comment] = commentFeed.getItems.asScala.toList
      printComments(comments)
    }
    catch {
      case e: IOException => System.err.println(e.getMessage)
      case t: Throwable => t.printStackTrace()
    }
    System.exit(1)
  }
  def printComments(comments: List[Comment]) {
    comments.foreach(c => {
      println("Poster's Name: " + c.getActor.getDisplayName)
      println("Poster's ID: " + c.getActor.getId)
      println("Comment Text: " + c.getObject.getContent)
      println("Comment ID: " + c.getId)
      println("Comments this replies to: " + c.getInReplyTo)
      println("Plus-one'rs: " + c.getPlusoners)
      println("eTag: " + c.getEtag)
      println("Kind: " + c.getKind)
      println("Published: " + c.getPublished)
      println("Updated: " + c.getUpdated)
      println("Verb: " + c.getVerb)
      println("Key-set: " + c.keySet())
      println("Values: " + c.values())
      println("Pretty string: " + c.toPrettyString)
      println("Simple string: " + c.toString)
      println("Just printed: " + c)
      println()
    })
  }
}
