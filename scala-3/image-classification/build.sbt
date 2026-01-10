ThisBuild / scalaVersion := "3.6.3"
ThisBuild / javacOptions ++= Seq("-source", "25", "-target", "25")

lazy val root = (project in file("."))
  .settings(
    name := "image-classification",
    version := "1.0",
    libraryDependencies ++= Seq(
      "ai.djl" % "api" % "0.30.0",
      "ai.djl.pytorch" % "pytorch-engine" % "0.30.0",
      "ai.djl.pytorch" % "pytorch-model-zoo" % "0.30.0",
      "org.slf4j" % "slf4j-simple" % "2.0.9",
      "org.scalatest" %% "scalatest" % "3.2.16" % Test
    )
  )
