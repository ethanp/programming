name := "CommentAnalyzer_0"

version := "1.0-SNAPSHOT"

libraryDependencies ++= Seq(
  jdbc,
  anorm,
  cache,
  "com.google.api-client" % "google-api-client" % "1.17.0-rc",
  "com.google.apis" % "google-api-services-plus" % "v1-rev116-1.17.0-rc",
  "com.google.gdata" % "core" % "1.47.1",
  "postgresql" % "postgresql" % "9.1-901.jdbc4",
  "edu.stanford.nlp" % "stanford-corenlp" % "3.3.0"
)

addCommandAlias("generate-project",
  ";update-classifiers;update-sbt-classifiers;gen-idea sbt-classifiers")

play.Project.playScalaSettings
