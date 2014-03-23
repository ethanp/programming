name := "akka-redis-websockets-play-scala_translation"

version := "1.0-SNAPSHOT"

libraryDependencies ++= Seq(
  jdbc,
  anorm,
  cache,
  "com.typesafe" %% "play-plugins-redis" % "2.1.1",
  "org.webjars" %% "webjars-play" % "2.2.1-2",
  "org.webjars" % "bootstrap" % "3.1.0"
)

resolvers += "org.sedis" at "http://pk11-scratch.googlecode.com/svn/trunk"

scalacOptions ++= Seq( "-deprecation", "-unchecked", "-feature" )

play.Project.playScalaSettings
