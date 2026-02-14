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
    assert(exception.getMessage.contains("positivo"))

  test("countFields should return correct count for Cliente"):
    assert(FieldCounter.countFields[Cliente] == 3)

  test("countFields should return correct count for Produto"):
    assert(FieldCounter.countFields[Produto] == 3)

  test("countFields should return correct count for Endereco"):
    assert(FieldCounter.countFields[Endereco] == 4)

  test("fieldNames should return correct names for Cliente"):
    val names = FieldCounter.fieldNames[Cliente]
    assert(names == List("nome", "cpf", "saldo"))

  test("fieldNames should return correct names for Produto"):
    val names = FieldCounter.fieldNames[Produto]
    assert(names == List("nome", "preco", "estoque"))
