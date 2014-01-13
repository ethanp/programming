package models
/**
 * Ethan Petuchowski
 * 1/9/14
 */

case class Video(id: Long, title: String, url: String) {

  // generate the url for embedding the video on a webpage:
  //  http://www.youtube.com/watch?v=sTuf3 => http://www.youtube.com/embed/sTuf3
  def urlForEmbedding: String = """.*//([^/]+)/.*=(.*)""".r.findFirstMatchIn(url) match {
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

  def add(video: Video) {
    videos += video
  }
}
