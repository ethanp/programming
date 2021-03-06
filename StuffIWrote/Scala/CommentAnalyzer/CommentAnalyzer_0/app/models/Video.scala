package models

/**
 * Ethan Petuchowski
 * 1/9/14
 */

import play.api.Play.current
import play.api.db.DB
import com.google.gdata.client.youtube.YouTubeService
import java.net.URL
import com.google.gdata.data.youtube.VideoEntry
import java.util.Date

case class Video(id: String, title: String, dateLastRetrieved: Option[Date]) {
  val urlForEmbedding: String = "//www.youtube.com/embed/"+id
  val urlForWebsite: String = "http://www.youtube.com/watch?v="+id
  def getComments: List[Comment] = Comment.getAllForVideoID(id)
  def avgCommentScore = Comment.avgCommentScoreForVideoID(id)
  def commentScoreStatement = {
    if (avgCommentScore > 2.1)
      "Wow!"
    else if (avgCommentScore > 1.6)
      "Meh."
    else
      "Bleh!"
  }
}

object Video {
//  var videos = Set(
//    Video("cdaAWFoWr2c", "R Kelly - Real Talk"),
//    Video("zEfIkuTtzQ4", "Parliament Funkadelic - Swing Down Sweet Chariot - 1976")
//  )
  def someURL(v: Video) = Some(v.urlForWebsite)
  // SQL Querying using Anorm
  import anorm.{SQL, SqlQuery}

  val sql: SqlQuery = SQL("select * from videos order by title asc")

  // Using the Anorm's Stream API
  def getAllWithAnorm: List[Video] = DB.withConnection {
    implicit connection =>
      sql(/*apply*/).map ( row /*SqlRow*/ =>
        Video(row[String]("id"), row[String]("title"), row[Option[Date]]("dateLastRetrieved"))
      ).toList
  }

  // Using Pattern Matching
  def getAllWithPatterns: List[Video] = DB.withConnection {
    implicit connection =>
      import anorm.Row
      sql().collect {
        /* NULL values would be a None */
        case Row(Some(id: String), Some(title: String), Some(dateLastRetrieved: Option[Date])) => Video(id, title, dateLastRetrieved)
      }.toList /*call toList on Stream to actually retrieve contents*/
  }

  // Using Parser Combinators  (the way to go, it seems)

  import anorm.RowParser
  import anorm.~
  import anorm.SqlParser._
  import anorm.ResultSetParser

  val videoParser: RowParser[Video] = {
    str("id") ~ str("title") ~ get[Option[Date]]("dateLastRetrieved") map {
       case id ~ title ~ dateLastRetrieved =>
      Video(id,  title,  dateLastRetrieved) /* turn (pattern) =into=> (this) */
    }
  }
  val videosParser: ResultSetParser[List[Video]] = videoParser *

  def getAllWithParser: List[Video] = DB.withConnection {
    implicit connection =>
      sql.as(videosParser)
  }

  // Insert, Update, Delete
  def insert(video: Video): Boolean = DB.withConnection {
    implicit connection =>
      val addedRows = SQL(
        /* Identifiers surrounded by curly braces denote named
           parameters to be mapped with the elements in on(...) */
        "insert into videos values ({id}, {title}, {dateLastRetrieved})").on(
          "id"                -> video.id,
          "title"             -> video.title,
          "dateLastRetrieved" -> video.dateLastRetrieved
        ).executeUpdate(/*implicit connection*/) /* returns num rows affected */
      addedRows == 1
  }

  def update(video: Video): Boolean = DB.withConnection {
    implicit connection =>
      val updatedRows = SQL("update videos set id = {id}, title = {title}, dateLastRetrieved = {dateLastRetrieved}").on(
        "id"                -> video.id,
        "title"             -> video.title,
        "dateLastRetrieved" -> video.dateLastRetrieved
      ).executeUpdate()
      updatedRows == 1
  }

  def delete(video: Video): Boolean = DB.withConnection {
    implicit connection =>
      val updatedRows = SQL("delete from videos where id = {id}").on(
        "id" -> video.id
      ).executeUpdate()
      updatedRows == 0  // why zero? this came from List-5.8 but is it right?
  }

  /**
   * Called by the Form on the +New page when a url is entered
   * Takes url, extracts the id, downloads the title, sets Now as the date last retrieved
   * Also goes and retrieves the video's comments
   */
  def makeVideoFromURL(url: String) = {
    val id = getIDFromURL(url)
    val lines = scala.io.Source.fromFile("/etc/googleIDKey").mkString.split("\n")
    val service: YouTubeService = new YouTubeService(lines(0), lines(1))
    val videoEntryUrl = "http://gdata.youtube.com/feeds/api/videos/" + id
    val videoEntry = service.getEntry(new URL(videoEntryUrl), classOf[VideoEntry])
    val title = videoEntry.getTitle.getPlainText
    Video(id, title, Some(new Date(/*right now*/)))
  }

  def getIDFromURL(url: String): String = """(http)?(s)?(://)?www.youtube.com/watch\?v=([^#&]*)""".r.findFirstMatchIn(url) match {
    case Some(m) => m.group(4)
    case _ => ""  // this works for the form because now it won't validate
  }

  def findByURL(url: String): Option[Video] = getAllWithParser.find(_.id == getIDFromURL(url))

  def findByID(id: String): Option[Video] = getAllWithParser.find(_.id == id)

//  def findAll: List[Video] = videos.sortBy(_.title)
//  def add(video: Video) {
//    videos += video
//  }
}
