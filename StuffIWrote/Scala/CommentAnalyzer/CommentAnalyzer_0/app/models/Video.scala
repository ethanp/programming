package models

import scala.util.matching.Regex

/**
 * Ethan Petuchowski
 * 1/9/14
 */

case class Video(id: Long, title: String, url: String) {

  // generate the url for embedding the video on a webpage
  def embed: String = """.*//([^/]+)/.*=(.*)""".r.findFirstMatchIn(url) match {
    case Some(m) => "//" + m.group(1) + "/embed/" + m.group(2)
    case _ => throw new RuntimeException("embedding URL couldn't be extracted from stored URL")
  }

}

object Video {
  var videos = Set(
    Video(1, "R Kelly - Real Talk", "http://www.youtube.com/watch?v=cdaAWFoWr2c"),
    Video(2, "Parliament Funkadelic - Swing Down Sweet Chariot - 1976", "http://www.youtube.com/watch?v=zEfIkuTtzQ4")
  )

  def findByID(id: Long): Option[Video] = videos.find(_.id == id)

  def findAll: List[Video] = videos.toList.sortBy(_.title)

  def createNew(title: String, url: String): Video = Video.apply(videos.size, title, url)

  def showExisting(id: Long): (Video) => Option[(Long, String, String)] = findByID(id) match {
    case Some(v) => Video.unapply
    case _ => None
  }
}
