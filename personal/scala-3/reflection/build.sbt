ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "3.6.3"
ThisBuild / javacOptions ++= Seq("-source", "25", "-target", "25")

lazy val root = (project in file("."))
  .settings(
    name := "reflection",
    version := "1.0",
    libraryDependencies ++= Seq(
      "org.scala-lang" %% "scala3-compiler" % "3.6.3",
      "org.scalatest" %% "scalatest" % "3.2.16" % Test
    )
  )

