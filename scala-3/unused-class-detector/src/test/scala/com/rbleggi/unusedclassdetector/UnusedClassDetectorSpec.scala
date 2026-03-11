package com.rbleggi.unusedclassdetector

import org.scalatest.funsuite.AnyFunSuite

class UnusedClassDetectorSpec extends AnyFunSuite {

  test("UnusedClassDetector should detect unused classes") {
    val code = """
      |class Used {}
      |class Unused {}
      |object Main extends App {
      |  val u = new Used()
      |}
      |""".stripMargin
    val detector = new UnusedClassDetector
    val unused = detector.findUnusedClasses(code)
    assert(unused.contains("Unused"))
    assert(!unused.contains("Used"))
  }
}
