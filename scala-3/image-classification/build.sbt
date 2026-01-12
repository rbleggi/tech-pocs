ThisBuild / scalaVersion := "3.6.3"
ThisBuild / javacOptions ++= Seq("-source", "25", "-target", "25")

lazy val root = (project in file("."))
  .settings(
    name := "image-classification",
    version := "1.0"
  )
