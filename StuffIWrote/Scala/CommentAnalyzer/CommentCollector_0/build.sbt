name := "Comment Collector"

organization := "na.ethanp"

version := "0.5"

scalaVersion := "2.10.3"

libraryDependencies ++= Seq(
  "org.scalatest" % "scalatest_2.10" % "2.0" % "test",
  "com.google.api-client" % "google-api-client" % "1.17.0-rc",
  "com.google.apis" % "google-api-services-plus" % "v1-rev116-1.17.0-rc",
  "com.google.gdata" % "core" % "1.47.1",
  "io.spray" %% "spray-json" % "1.2.5",
  "com.google.http-client" % "google-http-client-jackson2" % "1.17.0-rc",
  "edu.stanford.nlp" % "stanford-corenlp" % "3.3.0",
  "com.typesafe.akka" %% "akka-actor" % "2.2.3"
)

resolvers ++= Seq(
  "spray" at "http://repo.spray.io/",
  "Typesafe Repository" at "http://repo.typesafe.com/typesafe/releases/"
)

addCommandAlias("generate-project",
  ";update-classifiers;update-sbt-classifiers;gen-idea sbt-classifiers")
