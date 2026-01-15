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

  def calculateTax(state: String, year: Int, product: Product, price: Double): Double =
    defaultSpecs.find(_.isSatisfiedBy(state, year)) match
      case Some(spec) => spec.calculateTax(product, price)
      case None => throw Exception(s"No tax rule found for $state in the year $year")

@main def run(): Unit =
  val product1 = Product("Smartphone", "electronics")
  val product2 = Product("Rice", "food")
  val product3 = Product("History Book", "book")

  val calculator = TaxCalculator()

  val priceProduct1 = 2500.0
  val priceProduct2 = 20.0
  val priceProduct3 = 50.0

  println(f"Tax for ${product1.name} in SP (2024): RS ${calculator.calculateTax("SP", 2024, product1, priceProduct1)}%.2f")
  println(f"Tax for ${product2.name} in MG (2024): RS ${calculator.calculateTax("MG", 2024, product2, priceProduct2)}%.2f")
  println(f"Tax for ${product3.name} in RJ (2024): RS ${calculator.calculateTax("RJ", 2024, product3, priceProduct3)}%.2f")
  println(f"Tax for ${product1.name} in SP (2025): RS ${calculator.calculateTax("SP", 2025, product1, priceProduct1)}%.2f")