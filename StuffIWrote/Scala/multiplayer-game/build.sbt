name := "multiplayer-game"

version := "1.0-SNAPSHOT"

libraryDependencies ++= Seq(
  jdbc,
  anorm,
  cache,
  "org.webjars" %% "webjars-play" % "2.2.1-2",
  "org.webjars" % "bootstrap" % "3.1.0"
)

scalacOptions ++= Seq( "-deprecation", "-unchecked", "-feature" )

play.Project.playScalaSettings
