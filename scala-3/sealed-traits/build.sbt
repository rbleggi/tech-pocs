val scala3Version = "3.6.3"

lazy val root = project
  .in(file("."))
  .settings(
    name := "sealed-traits",
    version := "0.1.0-SNAPSHOT",
    scalaVersion := scala3Version,
    libraryDependencies += "org.scalatest" %% "scalatest" % "3.2.16" % Test
  )
