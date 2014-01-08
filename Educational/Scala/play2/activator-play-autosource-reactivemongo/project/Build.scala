// Note: This is an example Build.scala for a Play project

import sbt._
import Keys._
import play.Project._

object ApplicationBuild extends Build {

  val appName         = "activator-tmpl-autosource-reactivemongo"
  val appVersion      = "1.0-SNAPSHOT"

  val mandubianRepo = Seq(
    "Mandubian repository snapshots" at "https://github.com/mandubian/mandubian-mvn/raw/master/snapshots/",
    "Mandubian repository releases" at "https://github.com/mandubian/mandubian-mvn/raw/master/releases/"
  )

  val appDependencies = Seq(
    // Select Play modules
    //jdbc,      // The JDBC connection pool and the play.api.db API
    //anorm,     // Scala RDBMS Library
    //javaJdbc,  // Java database API
    //javaEbean, // Java Ebean plugin
    //javaJpa,   // Java JPA plugin
    //filters,   // A set of built-in filters
    //javaCore,  // The core Java API
  
    // Add your own project dependencies in the form:
    // "group" % "artifact" % "version"
    "play-autosource"   %% "reactivemongo"       % "0.1-SNAPSHOT"
  )

  val main = play.Project(appName, appVersion, appDependencies).settings(
    scalaVersion := "2.10.1",
    resolvers ++= mandubianRepo
    // Add your own project settings here      
  )

}
