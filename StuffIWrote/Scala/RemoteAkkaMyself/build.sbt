name := "RemoteAkkaMyself"

version := "1.0"

scalaVersion := "2.11.6"

val akkaVersion = "2.3.9"

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-remote" % akkaVersion,
  "com.typesafe.akka" %% "akka-cluster" % akkaVersion,
  "org.scalatest" %% "scalatest" % "2.2.2" % "test"
)

TaskKey[Unit]("execClient") := (runMain in Compile).toTask(" ethanp.cluster.Client 0").value

TaskKey[Unit]("execServer") := (runMain in Compile).toTask(" ethanp.cluster.Server 0").value
