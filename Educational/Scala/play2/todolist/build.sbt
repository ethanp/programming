name := "todolist"

version := "1.0-SNAPSHOT"

libraryDependencies ++= Seq(
  jdbc,
  anorm,
  cache
)

val appDependencies = Seq(
  jdbc
)

play.Project.playScalaSettings
