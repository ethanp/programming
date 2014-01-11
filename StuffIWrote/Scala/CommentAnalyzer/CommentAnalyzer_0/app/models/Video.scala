package models

/**
 * Ethan Petuchowski
 * 1/9/14
 */

case class Video(id: Long, url: String, title: String)

object Video {
  var videos = Set(
    Video(1, "http://www.youtube.com/watch?v=cdaAWFoWr2c", "R Kelly - Real Talk Behind the Scenes"),
    Video(2,"http://www.youtube.com/watch?v=zEfIkuTtzQ4",
      "Parliament Funkadelic - Swing Down Sweet Chariot - Mothership Connection - Houston 1976")
  )

  def findAll = videos.toList.sortBy(_.title)
}
