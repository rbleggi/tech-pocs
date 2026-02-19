package com.rbleggi.macros

import org.scalatest.funsuite.AnyFunSuite

class MacrosSpec extends AnyFunSuite:

  test("debug macro should return the value"):
    val result = DebugMacros.debug(42)
    assert(result == 42)

  test("debug macro should work with expressions"):
    val result = DebugMacros.debug(10 + 5)
    assert(result == 15)

  test("debug macro should work with string concatenation"):
    val result = DebugMacros.debug("Hello" + " " + "World")
    assert(result == "Hello World")

  test("assertPositive should return value when positive"):
    val result = DebugMacros.assertPositive(100.0)
    assert(result == 100.0)

  test("assertPositive should return value for small positive"):
    val result = DebugMacros.assertPositive(0.01)
    assert(result == 0.01)

  test("assertPositive should throw for zero"):
    assertThrows[IllegalArgumentException] {
      DebugMacros.assertPositive(0.0)
    }

  test("assertPositive should throw for negative"):
    assertThrows[IllegalArgumentException] {
      DebugMacros.assertPositive(-5.0)
    }

  test("assertPositive should throw for large negative"):
    val exception = intercept[IllegalArgumentException] {
      DebugMacros.assertPositive(-1000.0)
    }
    assert(exception.getMessage.contains("positive"))

  test("countFields should return correct count for Client"):
    assert(FieldCounter.countFields[Client] == 3)

  test("countFields should return correct count for Product"):
    assert(FieldCounter.countFields[Product] == 3)

  test("countFields should return correct count for Address"):
    assert(FieldCounter.countFields[Address] == 4)

  test("fieldNames should return correct names for Client"):
    val names = FieldCounter.fieldNames[Client]
    assert(names == List("name", "cpf", "balance"))

  test("fieldNames should return correct names for Product"):
    val names = FieldCounter.fieldNames[Product]
    assert(names == List("name", "price", "stock"))
