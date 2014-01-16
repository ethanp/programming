package models

/**
 * Ethan Petuchowski
 * 1/9/14
 */

case class Video(id: String, title: String) {
  def urlForEmbedding: String = "//www.youtube.com/embed/"+id
  def urlForWebsite: String = "http://www.youtube.com/watch?v="+id
}

object Video {
  var videos = Set(
    Video("cdaAWFoWr2c", "R Kelly - Real Talk"),
    Video("zEfIkuTtzQ4", "Parliament Funkadelic - Swing Down Sweet Chariot - 1976")
  )

  def makeVideoFromURL(url: String, title: String) = Video(getIDFromURL(url), title)

  def getIDFromURL(url: String): String = """(http)?(s)?(://)?www.youtube.com/watch\?v=(.*)""".r.findFirstMatchIn(url) match {
    case Some(m) => m.group(4)
    case _ => ""  // this works for the form because now it won't validate
  }

  def findByURL(url: String): Option[Video] = videos.find(_.id == getIDFromURL(url))

  def findByID(id: String): Option[Video] = videos.find(_.id == id)

  def findAll: List[Video] = videos.toList.sortBy(_.title)

  def add(video: Video) {
    videos += video
  }
}
