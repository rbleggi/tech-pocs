ThisBuild / scalaVersion := "3.6.0"

lazy val fileShareSystem = (project in file("."))
  .settings(
    name := "file-share-system",
    version := "0.1.0",
    libraryDependencies ++= Seq(
      "org.scalatest" %% "scalatest" % "3.2.16" % Test
    )
  )
