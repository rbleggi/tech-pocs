package com.rbleggi.converterframework

import org.scalatest.funsuite.AnyFunSuite

class ConverterFrameworkSpec extends AnyFunSuite {
  test("Converter should convert using provided function") {
    val intToString = Converter[Int, String](_.toString)
    assert(intToString.convert(42) == "42")
  }

  test("addressToString should convert Address to String") {
    val address = Address("123 Main St", "Springfield", "12345")
    val result = addressToString.convert(address)

    assert(result.contains("123 Main St"))
    assert(result.contains("Springfield"))
    assert(result.contains("12345"))
  }

  test("personToDTO should convert Person to PersonDTO") {
    val address = Address("123 Main St", "Springfield", "12345")
    val person = Person("John Doe", 30, address)

    val dto = personToDTO.convert(person)

    assert(dto.fullName == "John Doe")
    assert(dto.age == 30)
    assert(dto.city == "Springfield")
  }

  test("personToString should convert Person to String") {
    val address = Address("123 Main St", "Springfield", "12345")
    val person = Person("John Doe", 30, address)

    val result = personToString.convert(person)

    assert(result.contains("John Doe"))
    assert(result.contains("30"))
    assert(result.contains("Springfield"))
  }

  test("Converter should be composable") {
    val intToDouble = Converter[Int, Double](_.toDouble)
    val doubleToString = Converter[Double, String](d => f"$d%.2f")

    val intAsDouble = intToDouble.convert(42)
    val finalString = doubleToString.convert(intAsDouble)

    assert(finalString == "42.00")
  }

  test("Converter should handle complex transformations") {
    case class Source(value: Int)
    case class Target(doubled: Int)

    val converter = Converter[Source, Target](s => Target(s.value * 2))
    val result = converter.convert(Source(21))

    assert(result.doubled == 42)
  }
}
