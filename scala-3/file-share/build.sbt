ThisBuild / scalaVersion := "3.6.0"
ThisBuild / javacOptions ++= Seq("-source", "25", "-target", "25")

lazy val root = (project in file("."))
  .settings(
    name := "file-share",
    version := "0.1.0",
    libraryDependencies ++= Seq(
      "org.scalatest" %% "scalatest" % "3.2.16" % Test
    )
  )
