package com.rbleggi.unusedclassdetector

class UnusedClassDetectorSpec {

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
