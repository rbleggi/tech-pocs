package com.rbleggi.json4s

import org.scalatest.funsuite.AnyFunSuite
import org.json4s._
import org.json4s.native.JsonParser
import org.json4s.native.Serialization

class Json4sSpec extends AnyFunSuite {
  implicit val formats: Formats = DefaultFormats
  test("JsonParser parses valid JSON string") {
    val json = JsonParser.parse("""{"application": "MyApp", "version": 1.0}""")
    val app = (json \ "application").extractOpt[String]
    val version = (json \ "version").extractOpt[Double]
    assert(app.contains("MyApp"))
    assert(version.contains(1.0))
  }
}
