package com.rbleggi.reflection

import org.scalatest.funsuite.AnyFunSuite
import scala.reflect.ClassTag

class ReflectionSpec extends AnyFunSuite:

  test("getClassName should return full class name"):
    val person = Person("Alice", 30, "alice@example.com")
    val className = ReflectionUtils.getClassName(person)
    assert(className.contains("Person"))

  test("getSimpleClassName should return simple name"):
    val person = Person("Bob", 25, "bob@example.com")
    val simpleName = ReflectionUtils.getSimpleClassName(person)
    assert(simpleName == "Person")

  test("getFields should return field names"):
    val person = Person("Charlie", 35, "charlie@example.com")
    val fields = ReflectionUtils.getFields(person)
    assert(fields.contains("name"))
    assert(fields.contains("age"))
    assert(fields.contains("email"))

  test("getFields should work for Product"):
    val product = Product(1001L, "Laptop", 999.99, true)
    val fields = ReflectionUtils.getFields(product)
    assert(fields.contains("id"))
    assert(fields.contains("name"))
    assert(fields.contains("price"))
    assert(fields.contains("inStock"))

  test("getMethods should return method names"):
    val person = Person("Diana", 28, "diana@example.com")
    val methods = ReflectionUtils.getMethods(person)
    assert(methods.nonEmpty)

  test("getFieldValue should retrieve field value"):
    val person = Person("Eve", 32, "eve@example.com")
    val nameValue = ReflectionUtils.getFieldValue(person, "name")
    assert(nameValue == Some("Eve"))

  test("getFieldValue should retrieve age"):
    val person = Person("Frank", 40, "frank@example.com")
    val ageValue = ReflectionUtils.getFieldValue(person, "age")
    assert(ageValue == Some(40))

  test("getFieldValue should return None for non-existent field"):
    val person = Person("Grace", 27, "grace@example.com")
    val value = ReflectionUtils.getFieldValue(person, "nonExistentField")
    assert(value.isEmpty)

  test("invokeMethod should call method"):
    val str = "hello"
    val result = ReflectionUtils.invokeMethod(str, "toUpperCase")
    assert(result == Some("HELLO"))

  test("invokeMethod should return None for invalid method"):
    val str = "test"
    val result = ReflectionUtils.invokeMethod(str, "invalidMethod")
    assert(result.isEmpty)

  test("createInstance should create new instance"):
    val result = ReflectionUtils.createInstance(
      classOf[Person],
      "Henry",
      Integer.valueOf(45),
      "henry@example.com"
    )
    assert(result.isDefined)
    assert(result.get.name == "Henry")
    assert(result.get.age == 45)

  test("isInstanceOf should check instance type"):
    val person = Person("Iris", 29, "iris@example.com")
    assert(ReflectionUtils.isInstanceOf(person, classOf[Person]))
    assert(!ReflectionUtils.isInstanceOf(person, classOf[Product]))

  test("toMap should convert object to map"):
    val person = Person("Jack", 33, "jack@example.com")
    val map = SerializationUtils.toMap(person)
    assert(map("name") == "Jack")
    assert(map("age") == 33)
    assert(map("email") == "jack@example.com")

  test("toKeyValuePairs should convert to string pairs"):
    val person = Person("Kate", 26, "kate@example.com")
    val pairs = SerializationUtils.toKeyValuePairs(person)
    assert(pairs.exists { case (k, v) => k == "name" && v == "Kate" })

  test("prettyPrint should format object"):
    val person = Person("Leo", 31, "leo@example.com")
    val pretty = SerializationUtils.prettyPrint(person)
    assert(pretty.contains("Person"))
    assert(pretty.contains("Leo"))

  test("JsonSerializer should serialize string"):
    val json = JsonSerializer.toJson("test")
    assert(json == "\"test\"")

  test("JsonSerializer should serialize int"):
    val json = JsonSerializer.toJson(42)
    assert(json == "42")

  test("JsonSerializer should serialize boolean"):
    val json = JsonSerializer.toJson(true)
    assert(json == "true")

  test("JsonSerializer should serialize null"):
    val json = JsonSerializer.toJson(null)
    assert(json == "null")

  test("JsonSerializer should serialize Person"):
    val person = Person("Mike", 34, "mike@example.com")
    val json = JsonSerializer.toJson(person)
    assert(json.contains("name"))
    assert(json.contains("Mike"))
    assert(json.contains("age"))

  test("JsonSerializer should serialize Product"):
    val product = Product(2002L, "Mouse", 29.99, false)
    val json = JsonSerializer.toJson(product)
    assert(json.contains("id"))
    assert(json.contains("name"))
    assert(json.contains("Mouse"))
    assert(json.contains("price"))

  test("Person case class should create instance"):
    val person = Person("Nancy", 38, "nancy@example.com")
    assert(person.name == "Nancy")
    assert(person.age == 38)
    assert(person.email == "nancy@example.com")

  test("Product case class should create instance"):
    val product = Product(3003L, "Keyboard", 79.99, true)
    assert(product.id == 3003L)
    assert(product.name == "Keyboard")
    assert(product.price == 79.99)
    assert(product.inStock == true)
