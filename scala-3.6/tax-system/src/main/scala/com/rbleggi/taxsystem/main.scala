package com.rbleggi.taxsystem

import com.rbleggi.taxsystem.model.*
import com.rbleggi.taxsystem.service.TaxCalculator

@main def run(): Unit = {
  val laptop = Electronic("Laptop", 1500.0)
  val book = Book("Scala Programming", 30.0)
  val pizza = Food("Pizza", 15.0)

  val california = State("California", "CA")
  val texas = State("Texas", "TX")

  val taxRules = List(
    TaxRule(california, laptop, 2025, 8.5),
    TaxRule(texas, laptop, 2025, 6.25),
    TaxRule(california, book, 2025, 0.0),
    TaxRule(texas, pizza, 2025, 4.0)
  )

  val taxCalculator = new TaxCalculator(taxRules)

  println(s"Total price for Laptop in CA: $$${taxCalculator.calculateTotalPrice(laptop, california, 2025)}")
  println(s"Total price for Laptop in TX: $$${taxCalculator.calculateTotalPrice(laptop, texas, 2025)}")
  println(s"Total price for Book in CA: $$${taxCalculator.calculateTotalPrice(book, california, 2025)}")
  println(s"Total price for Pizza in TX: $$${taxCalculator.calculateTotalPrice(pizza, texas, 2025)}")
}
