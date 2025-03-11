package com.rbleggi.taxsystem.service

import com.rbleggi.taxsystem.model.{Book, Electronic, State, TaxRule}
import org.scalatest.funsuite.AnyFunSuite

class TaxCalculatorTest extends AnyFunSuite {

  val laptop = Electronic("Laptop", 1500.0)
  val book = Book("Book", 30.0)

  val california = State("California", "CA")
  val texas = State("Texas", "TX")

  val taxRules = List(
    TaxRule(california, laptop, 2025, 8.5),
    TaxRule(texas, laptop, 2025, 6.25),
    TaxRule(california, book, 2025, 0.0)
  )

  val taxCalculator = new TaxCalculator(taxRules)

  test("Calculate total price for Laptop in California") {
    assert(taxCalculator.calculateTotalPrice(laptop, california, 2025) === 1627.5)
  }

  test("Calculate total price for Laptop in Texas") {
    assert(taxCalculator.calculateTotalPrice(laptop, texas, 2025) === 1593.75)
  }

  test("Calculate total price for Book in California (tax-free)") {
    assert(taxCalculator.calculateTotalPrice(book, california, 2025) === 30.0)
  }

  test("Return product price when no tax rule exists") {
    val unknownState = State("New York", "NY")
    assert(taxCalculator.calculateTotalPrice(laptop, unknownState, 2025) === 1500.0)
  }
}
