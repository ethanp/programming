name := "CommentAnalyzer_0"

version := "1.0-SNAPSHOT"

libraryDependencies ++= Seq(
  jdbc,
  anorm,
  cache,
  "com.google.api.client" % "google-api-client" % "1.0.10-alpha",
  "com.google.gdata" % "core" % "1.47.1"
)

val appDependencies = Seq(
  jdbc
)

play.Project.playScalaSettings
