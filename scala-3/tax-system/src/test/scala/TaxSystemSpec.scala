package com.rbleggi.taxsystem

import org.scalatest.funsuite.AnyFunSuite

class TaxSystemSpec extends AnyFunSuite:
  val calculator = TaxCalculator()

  test("DefaultTaxSpecification - electronics in MG 2024"):
    val product = Product("Tablet", "electronics")
    val tax = calculator.calculateTax("MG", 2024, product, 1000.0)
    assert(tax == 150.0)

  test("DefaultTaxSpecification - food in MG 2024"):
    val product = Product("Rice", "food")
    val tax = calculator.calculateTax("MG", 2024, product, 100.0)
    assert(tax == 5.0)

  test("DefaultTaxSpecification - book in MG 2024"):
    val product = Product("Novel", "book")
    val tax = calculator.calculateTax("MG", 2024, product, 100.0)
    assert(tax == 1.0)

  test("LuxuryTaxSpecification - electronics above threshold in SP 2024"):
    val product = Product("Smartphone", "electronics")
    val tax = calculator.calculateTax("SP", 2024, product, 2500.0)
    assert(tax == 575.0)

  test("LuxuryTaxSpecification - electronics below threshold in SP 2024"):
    val product = Product("Smartphone", "electronics")
    val tax = calculator.calculateTax("SP", 2024, product, 1000.0)
    assert(tax == 180.0)

  test("LuxuryTaxSpecification - electronics above threshold in RJ 2024"):
    val product = Product("Laptop", "electronics")
    val tax = calculator.calculateTax("RJ", 2024, product, 3000.0)
    assert(tax == 840.0)

  test("LuxuryTaxSpecification - food not affected by luxury tax"):
    val product = Product("Rice", "food")
    val tax = calculator.calculateTax("SP", 2024, product, 5000.0)
    assert(math.abs(tax - 350.0) < 0.01)

  test("ExemptTaxSpecification - food exempt in PR 2024"):
    val product = Product("Rice", "food")
    val tax = calculator.calculateTax("PR", 2024, product, 100.0)
    assert(tax == 0.0)

  test("ExemptTaxSpecification - book exempt in PR 2024"):
    val product = Product("Novel", "book")
    val tax = calculator.calculateTax("PR", 2024, product, 50.0)
    assert(tax == 0.0)

  test("ExemptTaxSpecification - book exempt in RS 2024"):
    val product = Product("Novel", "book")
    val tax = calculator.calculateTax("RS", 2024, product, 100.0)
    assert(tax == 0.0)

  test("ProgressiveTaxSpecification - tier 1 in BA 2024"):
    val product = Product("Item", "electronics")
    val tax = calculator.calculateTax("BA", 2024, product, 80.0)
    assert(tax == 4.0)

  test("ProgressiveTaxSpecification - tier 2 in BA 2024"):
    val product = Product("Item", "electronics")
    val tax = calculator.calculateTax("BA", 2024, product, 300.0)
    assert(tax == 30.0)

  test("ProgressiveTaxSpecification - tier 3 in BA 2024"):
    val product = Product("Item", "electronics")
    val tax = calculator.calculateTax("BA", 2024, product, 800.0)
    assert(tax == 120.0)

  test("ProgressiveTaxSpecification - tier 4 in BA 2024"):
    val product = Product("Item", "electronics")
    val tax = calculator.calculateTax("BA", 2024, product, 2000.0)
    assert(tax == 400.0)

  test("should throw exception for unknown state/year combination"):
    val product = Product("TV", "electronics")
    assertThrows[Exception]:
      calculator.calculateTax("XX", 2024, product, 1000.0)

  test("should throw exception for unknown year"):
    val product = Product("TV", "electronics")
    assertThrows[Exception]:
      calculator.calculateTax("SP", 2020, product, 1000.0)
