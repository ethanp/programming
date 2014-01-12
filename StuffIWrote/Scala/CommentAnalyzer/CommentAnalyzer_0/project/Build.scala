import sbt._
import Keys._
import play.Project._

object ApplicationBuild extends Build {

  val appName = "CommentAnalyzer"
  val appVersion = "1.0-SNAPSHOT"

  val appDependencies = Seq(
    "org.webjars" % "webjars-play_2.10" % "2.2.0",
    "org.webjars" % "bootstrap" % "2.3.1",
    "org.webjars" % "html5shiv" % "3.6.1",
    "org.webjars" % "bootswatch" % "2.3.1",
    "org.webjars" % "bootstrap-datepicker" % "1.0.1",
    "org.webjars" % "bootstrap-timepicker" % "0.2.1",

    "com.typesafe" %% "play-plugins-mailer" % "2.1-RC2",
    // Add your project dependencies here,
    javaCore,
    javaJdbc,
    javaEbean
  )

  val main = play.Project(appName, appVersion, appDependencies).settings(
    // Add your own project settings here
    resolvers += Resolver.url("sbt-plugin-snapshots", new URL("http://repo.scala-sbt.org/scalasbt/sbt-plugin-snapshots/"))(Resolver.ivyStylePatterns)
  )

}
