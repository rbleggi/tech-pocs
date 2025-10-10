package com.rbleggi.json4s

import org.json4s.*
import org.json4s.jackson.JsonMethods.*
import org.json4s.jackson.Serialization
import org.json4s.jackson.Serialization.{read, write}
import org.json4s.JsonDSL.*

import scala.math.BigDecimal.RoundingMode.HALF_UP

object Json4sQuickDemo extends App {

  // Normalize price to BigDecimal(2 dp) and add netPrice (if internal) ---

  def toMoney(j: JValue): Option[JDecimal] = j match {
    case JInt(n)      => Some(JDecimal(BigDecimal(n).setScale(2)))
    case JDouble(d)   => Some(JDecimal(BigDecimal(d).setScale(2, HALF_UP)))
    case JDecimal(bd) => Some(JDecimal(bd.setScale(2, HALF_UP)))
    case JString(s)   => Try(BigDecimal(s)).toOption.map(_.setScale(2, HALF_UP)).map(JDecimal(_))
    case _            => None
  }


  implicit val formats: Formats = DefaultFormats + AnimalSerializer

  val json = parse("""{
    "application": {
      "name": "MyApp",
      "config": {
        "database": {
          "host": "localhost",
          "port": 5432,
          "credentials": {
            "username": "admin",
            "password": "secret"
          }
        },
        "api": {
          "endpoints": [
            { "path": "/users", "auth": true },
            { "path": "/public", "auth": false }
          ]
        }
      }
    }
  }""")

  // 1. Extract nested fields and lists using \ and full path
  val dbHost = (json \ "application" \ "config" \ "database" \ "host").extract[String]
  println(s"Database host: $dbHost")

  // collect all JBool values for 'auth'
  val allAuthValues = (json \\ "auth").children.collect { case JBool(b) => b }
  println(s"All 'auth' values in endpoints: $allAuthValues")

  // Extract an optional field (may or may not exist)
  val maybeVersion: Option[String] = (json \\ "version").extractOpt[String]
  println(s"Optional version field: $maybeVersion")

  // 3. Serialize and deserialize an ADT (Animal)
  sealed trait Animal
  case class Dog(name: String) extends Animal
  case class Cat(name: String) extends Animal

  object AnimalSerializer extends CustomSerializer[Animal](_ => (
    {
      case JObject(JField("type", JString("dog")) :: JField("name", JString(name)) :: Nil) => Dog(name)
      case JObject(JField("type", JString("cat")) :: JField("name", JString(name)) :: Nil) => Cat(name)
    },
    {
      case Dog(name) => JObject(JField("type", JString("dog")), JField("name", JString(name)))
      case Cat(name) => JObject(JField("type", JString("cat")), JField("name", JString(name)))
    }
  ))

  val animal: Animal = Dog("Rex")
  val animalJson = write(animal)
  println("\nSerialized Animal (Dog):\n" + animalJson)
  val animalBack = read[Animal](animalJson)
  println("\nDeserialized Animal (Dog):\n" + animalBack)
}