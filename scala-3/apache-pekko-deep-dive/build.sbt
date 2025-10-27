ThisBuild / scalaVersion := "3.6.0"
ThisBuild / javacOptions ++= Seq("-source", "25", "-target", "25")

lazy val root = (project in file("."))
  .settings(
    name := "pekko-demo",
    version := "0.1.0",
    libraryDependencies ++= Seq(
      "org.apache.pekko" %% "pekko-actor-typed" % "1.0.2",
      "org.apache.pekko" %% "pekko-actor-testkit-typed" % "1.0.2" % Test,
      "org.scalatest" %% "scalatest" % "3.2.19" % Test
    )
  )
