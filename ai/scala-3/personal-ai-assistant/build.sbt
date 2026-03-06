ThisBuild / scalaVersion := "3.6.3"
ThisBuild / javacOptions ++= Seq("-source", "25", "-target", "25")

lazy val root = project
  .in(file("."))
  .settings(
    name := "personal-ai-assistant",
    version := "0.1.0-SNAPSHOT",
    libraryDependencies += "org.scalatest" %% "scalatest" % "3.2.16" % Test
  )
