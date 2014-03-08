name := "Google Data With Akka"

version := "1.0"

scalaVersion := "2.10.2"

organization := "edu.utexas"

libraryDependencies ++= {
  	Seq(
		    "org.specs2"    %% "specs2"    % "2.1.1" % "test",
    		"org.scalatest" %% "scalatest" % "1.9.1" % "test",
        // "com.twitter"   %% "algebird"  % "0.2.0",
        "com.typesafe.akka" %% "akka-actor" % "2.2.3"
  	)
}

// if you have more than one main method, you can specify which is used when typing 'run' in sbt
// mainclass := Some("edu.utexas.App")

resolvers ++= Seq(
                "snapshots"           at "http://oss.sonatype.org/content/repositories/snapshots",
                "releases"            at "http://oss.sonatype.org/content/repositories/releases",
                "Typesafe Repository" at "http://repo.typesafe.com/typesafe/releases/"
                )

scalacOptions ++= Seq("-unchecked", "-deprecation", "-feature")

// append several options to the list of options passed to the Java compiler
// javacOptions ++= Seq("-source", "1.5", "-target", "1.5")

// set the main class for packaging the main jar
// 'run' will still auto-detect and prompt
// change Compile to Test to set it for the test jar
// mainClass in (Compile, packageBin) := Some("myproject.MyMain")

// set the main class for the main 'run' task
// change Compile to Test to set it for 'test:run'
// mainClass in (Compile, run) := Some("myproject.MyMain")

// set Ivy logging to be at the highest level
// ivyLoggingLevel := UpdateLogging.Full

// disable updating dynamic revisions (including -SNAPSHOT versions)
// offline := true

// fork a new JVM for 'run' and 'test:run'
// fork := true

// fork a new JVM for 'test:run', but not 'run'
// fork in Test := true

// add a JVM option to use when forking a JVM for 'run'
// javaOptions += "-Xmx2G"

// only use a single thread for building
// parallelExecution := false

// Execute tests in the current project serially
//   Tests from other projects may still run concurrently.
// parallelExecution in Test := false

// set the location of the JDK to use for compiling Java code.
// if 'fork' is true, this is used for 'run' as well
// javaHome := Some(file("/usr/lib/jvm/sun-jdk-1.6"))

// Use Scala from a directory on the filesystem instead of retrieving from a repository
// scalaHome := Some(file("/home/user/scala/trunk/"))

// don't aggregate clean (See FullConfiguration for aggregation details)
// aggregate in clean := false

// only show warnings and errors on the screen for compilations.
//  this applies to both test:compile and compile and is Info by default
// logLevel in compile := Level.Info

// only show warnings and errors on the screen for all tasks (the default is Info)
//  individual tasks can then be more verbose using the previous setting
// logLevel := Level.Info

// only store messages at info and above (the default is Debug)
//   this is the logging level for replaying logging with 'last'
// persistLogLevel := Level.Debug

// only show 10 lines of stack traces
// traceLevel := 10

// only show stack traces up to the first sbt stack frame
// traceLevel := 0

// publish test jar, sources, and docs
// publishArtifact in Test := true
