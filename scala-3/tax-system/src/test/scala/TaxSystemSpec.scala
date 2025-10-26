package com.rbleggi.taxsystem

import com.rbleggi.taxsystem.model.Product
import com.rbleggi.taxsystem.service.TaxCalculator

class TaxSystemSpec {
  val calculator = new TaxCalculator()

  test("TaxCalculator should calculate tax for electronics in SP 2024") {
    val product = Product("Smartphone", "electronics")
    val price = 1000.0
    val tax = calculator.calculateTax("SP", 2024, product, price)
    assert(tax == 180.0)
  }

  test("TaxCalculator should calculate tax for food in SP 2024") {
    val product = Product("Rice", "food")
    val price = 100.0
    val tax = calculator.calculateTax("SP", 2024, product, price)
    assert(tax == 7.0)
  }

  test("TaxCalculator should calculate tax for books in SP 2024") {
    val product = Product("History Book", "book")
    val price = 50.0
    val tax = calculator.calculateTax("SP", 2024, product, price)
    assert(tax == 0.0)
  }

  test("TaxCalculator should calculate tax for electronics in RJ 2024") {
    val product = Product("Laptop", "electronics")
    val price = 2000.0
    val tax = calculator.calculateTax("RJ", 2024, product, price)
    assert(tax == 400.0)
  }

  test("TaxCalculator should calculate tax for food in RJ 2024") {
    val product = Product("Beans", "food")
    val price = 50.0
    val tax = calculator.calculateTax("RJ", 2024, product, price)
    assert(tax == 4.0)
  }

  test("TaxCalculator should calculate tax for books in RJ 2024") {
    val product = Product("Math Book", "book")
    val price = 80.0
    val tax = calculator.calculateTax("RJ", 2024, product, price)
    assert(tax == 1.6)
  }

  test("TaxCalculator should calculate tax for electronics in MG 2024") {
    val product = Product("Tablet", "electronics")
    val price = 1500.0
    val tax = calculator.calculateTax("MG", 2024, product, price)
    assert(tax == 225.0)
  }

  test("TaxCalculator should calculate tax for food in MG 2024") {
    val product = Product("Milk", "food")
    val price = 10.0
    val tax = calculator.calculateTax("MG", 2024, product, price)
    assert(tax == 0.5)
  }

  test("TaxCalculator should calculate tax for books in MG 2024") {
    val product = Product("Science Book", "book")
    val price = 100.0
    val tax = calculator.calculateTax("MG", 2024, product, price)
    assert(tax == 1.0)
  }

  test("TaxCalculator should calculate tax for electronics in SP 2025") {
    val product = Product("Phone", "electronics")
    val price = 3000.0
    val tax = calculator.calculateTax("SP", 2025, product, price)
    assert(tax == 570.0)
  }

  test("TaxCalculator should calculate tax for food in SP 2025") {
    val product = Product("Bread", "food")
    val price = 20.0
    val tax = calculator.calculateTax("SP", 2025, product, price)
    assert(tax == 1.2)
  }

  test("TaxCalculator should calculate tax for books in SP 2025") {
    val product = Product("Novel", "book")
    val price = 60.0
    val tax = calculator.calculateTax("SP", 2025, product, price)
    assert(tax == 0.0)
  }

  test("TaxCalculator should throw exception for unknown state") {
    val product = Product("TV", "electronics")
    val price = 1000.0
    assertThrows[Exception] {
      calculator.calculateTax("BA", 2024, product, price)
    }
  }

  test("TaxCalculator should throw exception for unknown year") {
    val product = Product("TV", "electronics")
    val price = 1000.0
    assertThrows[Exception] {
      calculator.calculateTax("SP", 2023, product, price)
    }
  }

  test("TaxCalculator should handle zero price") {
    val product = Product("Free Item", "electronics")
    val price = 0.0
    val tax = calculator.calculateTax("SP", 2024, product, price)
    assert(tax == 0.0)
  }

  test("TaxCalculator should handle unknown category with zero tax") {
    val product = Product("Unknown", "unknown_category")
    val price = 100.0
    val tax = calculator.calculateTax("SP", 2024, product, price)
    assert(tax == 0.0)
  }
}
