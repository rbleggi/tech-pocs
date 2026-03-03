package com.rbleggi.json4s

import org.json4s._
import org.json4s.native.JsonParser
import org.json4s.native.Serialization
import org.scalatest.funsuite.AnyFunSuite

case class Person(name: String, age: Int)
case class Product(id: Int, name: String, price: Double)

class Json4sSpec extends AnyFunSuite {
  implicit val formats: Formats = DefaultFormats
  test("JsonParser parses valid JSON string") {
    val json = JsonParser.parse("""{"application": "MyApp", "version": 1.0}""")
    val app = (json \ "application").extractOpt[String]
    val version = (json \ "version").extractOpt[Double]
    assert(app.contains("MyApp"))
    assert(version.contains(1.0))
  }

  test("JsonParser should parse nested JSON objects") {
    val json = JsonParser.parse("""{"user": {"name": "John", "age": 30}}""")
    val userName = (json \ "user" \ "name").extractOpt[String]
    val userAge = (json \ "user" \ "age").extractOpt[Int]
    assert(userName.contains("John"))
    assert(userAge.contains(30))
  }

  test("JsonParser should parse JSON arrays") {
    val json = JsonParser.parse("""{"items": [1, 2, 3]}""")
    val items = (json \ "items").extractOpt[List[Int]]
    assert(items.contains(List(1, 2, 3)))
  }

  test("JsonParser should handle missing fields") {
    val json = JsonParser.parse("""{"name": "Test"}""")
    val missing = (json \ "nonexistent").extractOpt[String]
    assert(missing.isEmpty)
  }

  test("Serialization should convert case class to JSON") {
    val person = Person("Alice", 25)
    val jsonString = Serialization.write(person)
    assert(jsonString.contains("Alice"))
    assert(jsonString.contains("25"))
  }

  test("Serialization should parse JSON to case class") {
    val jsonString = """{"id":1,"name":"Widget","price":9.99}"""
    val product = Serialization.read[Product](jsonString)
    assert(product.id == 1)
    assert(product.name == "Widget")
    assert(product.price == 9.99)
  }

  test("JsonParser should handle boolean values") {
    val json = JsonParser.parse("""{"active": true, "deleted": false}""")
    val active = (json \ "active").extractOpt[Boolean]
    val deleted = (json \ "deleted").extractOpt[Boolean]
    assert(active.contains(true))
    assert(deleted.contains(false))
  }

  test("JsonParser should handle null values") {
    val json = JsonParser.parse("""{"value": null}""")
    val value = (json \ "value")
    assert(value == JNull)
  }
}
