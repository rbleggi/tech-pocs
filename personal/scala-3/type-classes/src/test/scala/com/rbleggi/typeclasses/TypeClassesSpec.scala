package com.rbleggi.typeclasses

import org.scalatest.funsuite.AnyFunSuite
import java.time.LocalDate

class TypeClassesSpec extends AnyFunSuite:

  test("Double formatter should format currency in BRL"):
    val price = 1250.50
    val result = price.display
    assert(result == "R$ 1250.50")

  test("Double formatter should format zero correctly"):
    val price = 0.0
    val result = price.display
    assert(result == "R$ 0.00")

  test("LocalDate formatter should format in Brazilian format"):
    val date = LocalDate.of(2024, 2, 14)
    val result = date.display
    assert(result == "14/02/2024")

  test("LocalDate formatter should format different date"):
    val date = LocalDate.of(2023, 12, 31)
    val result = date.display
    assert(result == "31/12/2023")

  test("String formatter should convert to uppercase"):
    val name = "joao silva"
    val result = name.display
    assert(result == "JOAO SILVA")

  test("CPF formatter should format numeric CPF"):
    val cpf = Cpf("12345678901")
    val result = cpf.display
    assert(result == "123.456.789-01")

  test("CPF formatter should format another CPF"):
    val cpf = Cpf("98765432100")
    val result = cpf.display
    assert(result == "987.654.321-00")

  test("CPF formatter should handle already formatted CPF"):
    val cpf = Cpf("123.456.789-01")
    val result = cpf.display
    assert(result == "123.456.789-01")

  test("Product formatter should format product with price"):
    val product = Product("Notebook Dell", 3500.00, "Electronics")
    val result = product.display
    assert(result == "Notebook Dell - R$ 3500.00 (Electronics)")

  test("Product formatter should format different product"):
    val product = Product("Mouse", 45.90, "Accessories")
    val result = product.display
    assert(result == "Mouse - R$ 45.90 (Accessories)")

  test("Formatter.format should work with using clause"):
    val price = 999.99
    val result = Formatter.format(price)
    assert(result == "R$ 999.99")

  test("Context bound should work correctly"):
    def display[T: Formatter](value: T): String =
      Formatter.format(value)

    val result = display(1500.75)
    assert(result == "R$ 1500.75")
