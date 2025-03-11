package com.rbleggi.taxsystem.model

final case class TaxRule(state: State, product: Product, year: Int, taxRate: Double) {
  def calculateTax: Double = product.price * (taxRate / 100)
}
