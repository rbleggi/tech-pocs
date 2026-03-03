package com.rbleggi.euler

import scala.io.Source

@main def runP22(): Unit = {
  val resource = getClass.getResourceAsStream("/names.txt")
  val names = Source.fromInputStream(resource).mkString.replaceAll("\"", "").split(",").sorted

  def nameValue(name: String): Int = name.map(_ - 'A' + 1).sum

  val totalScore = names.zipWithIndex.map { case (name, idx) =>
    nameValue(name) * (idx + 1)
  }.sum

  println(totalScore)
}
