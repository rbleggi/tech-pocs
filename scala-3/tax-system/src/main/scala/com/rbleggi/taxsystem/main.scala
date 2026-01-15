package com.rbleggi.taxsystem

case class Product(name: String, category: String)

case class TaxConfiguration(state: String, year: Int, rates: Map[String, Double])

trait TaxSpecification:
  def isSatisfiedBy(state: String, year: Int): Boolean
  def calculateTax(product: Product, price: Double): Double

class DefaultTaxSpecification(config: TaxConfiguration) extends TaxSpecification:
  override def isSatisfiedBy(state: String, year: Int): Boolean =
    config.state == state && config.year == year

  override def calculateTax(product: Product, price: Double): Double =
    price * config.rates.getOrElse(product.category, 0.0)

class LuxuryTaxSpecification(state: String, year: Int, threshold: Double, luxuryRate: Double) extends TaxSpecification:
  override def isSatisfiedBy(s: String, y: Int): Boolean =
    state == s && year == y

  override def calculateTax(product: Product, price: Double): Double =
    if price > threshold && product.category == "electronics" then
      price * luxuryRate
    else 0.0

class ExemptTaxSpecification(state: String, year: Int, exemptCategories: Set[String]) extends TaxSpecification:
  override def isSatisfiedBy(s: String, y: Int): Boolean =
    state == s && year == y

  override def calculateTax(product: Product, price: Double): Double =
    if exemptCategories.contains(product.category) then 0.0
    else -1.0

class ProgressiveTaxSpecification(state: String, year: Int, tiers: List[(Double, Double)]) extends TaxSpecification:
  override def isSatisfiedBy(s: String, y: Int): Boolean =
    state == s && year == y

  override def calculateTax(product: Product, price: Double): Double =
    tiers.find((threshold, _) => price <= threshold) match
      case Some((_, rate)) => price * rate
      case None => price * tiers.last._2

class TaxCalculator:
  private val defaultSpecs: List[TaxSpecification] = List(
    DefaultTaxSpecification(TaxConfiguration("SP", 2024, Map("electronics" -> 0.18, "food" -> 0.07, "book" -> 0.00))),
    DefaultTaxSpecification(TaxConfiguration("RJ", 2024, Map("electronics" -> 0.20, "food" -> 0.08, "book" -> 0.02))),
    DefaultTaxSpecification(TaxConfiguration("MG", 2024, Map("electronics" -> 0.15, "food" -> 0.05, "book" -> 0.01))),
    DefaultTaxSpecification(TaxConfiguration("SP", 2025, Map("electronics" -> 0.19, "food" -> 0.06, "book" -> 0.00)))
  )

  private val luxurySpecs: List[TaxSpecification] = List(
    LuxuryTaxSpecification("SP", 2024, 2000.0, 0.05),
    LuxuryTaxSpecification("RJ", 2024, 1500.0, 0.08),
    LuxuryTaxSpecification("SP", 2025, 2000.0, 0.06)
  )

  private val exemptSpecs: List[TaxSpecification] = List(
    ExemptTaxSpecification("PR", 2024, Set("food", "book")),
    ExemptTaxSpecification("RS", 2024, Set("book"))
  )

  private val progressiveSpecs: List[TaxSpecification] = List(
    ProgressiveTaxSpecification("BA", 2024, List((100.0, 0.05), (500.0, 0.10), (1000.0, 0.15), (Double.MaxValue, 0.20)))
  )

  def calculateTax(state: String, year: Int, product: Product, price: Double): Double =
    val exemptSpec = exemptSpecs.find(_.isSatisfiedBy(state, year))
    exemptSpec.map(_.calculateTax(product, price)) match
      case Some(tax) if tax == 0.0 => 0.0
      case _ =>
        val progressiveSpec = progressiveSpecs.find(_.isSatisfiedBy(state, year))
        progressiveSpec match
          case Some(spec) => spec.calculateTax(product, price)
          case None =>
            val baseTax = defaultSpecs.find(_.isSatisfiedBy(state, year)) match
              case Some(spec) => spec.calculateTax(product, price)
              case None => throw Exception(s"No tax rule found for $state in the year $year")
            val luxuryTax = luxurySpecs.find(_.isSatisfiedBy(state, year)).map(_.calculateTax(product, price)).getOrElse(0.0)
            baseTax + luxuryTax

@main def run(): Unit =
  val smartphone = Product("Smartphone", "electronics")
  val rice = Product("Rice", "food")
  val book = Product("History Book", "book")
  val laptop = Product("Laptop", "electronics")

  val calculator = TaxCalculator()

  println("=== DefaultTaxSpecification ===")
  println(f"Smartphone in MG (2024) - RS 1000: RS ${calculator.calculateTax("MG", 2024, smartphone, 1000.0)}%.2f")
  println(f"Rice in MG (2024) - RS 20: RS ${calculator.calculateTax("MG", 2024, rice, 20.0)}%.2f")

  println("\n=== LuxuryTaxSpecification (base + luxury) ===")
  println(f"Smartphone in SP (2024) - RS 2500: RS ${calculator.calculateTax("SP", 2024, smartphone, 2500.0)}%.2f")
  println(f"Laptop in RJ (2024) - RS 3000: RS ${calculator.calculateTax("RJ", 2024, laptop, 3000.0)}%.2f")

  println("\n=== ExemptTaxSpecification ===")
  println(f"Rice in PR (2024) - RS 50: RS ${calculator.calculateTax("PR", 2024, rice, 50.0)}%.2f")
  println(f"Book in RS (2024) - RS 100: RS ${calculator.calculateTax("RS", 2024, book, 100.0)}%.2f")

  println("\n=== ProgressiveTaxSpecification ===")
  println(f"Smartphone in BA (2024) - RS 80: RS ${calculator.calculateTax("BA", 2024, smartphone, 80.0)}%.2f")
  println(f"Smartphone in BA (2024) - RS 300: RS ${calculator.calculateTax("BA", 2024, smartphone, 300.0)}%.2f")
  println(f"Smartphone in BA (2024) - RS 800: RS ${calculator.calculateTax("BA", 2024, smartphone, 800.0)}%.2f")
  println(f"Laptop in BA (2024) - RS 2000: RS ${calculator.calculateTax("BA", 2024, laptop, 2000.0)}%.2f")