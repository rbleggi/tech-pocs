package com.rbleggi.taxsystem.service

import com.rbleggi.taxsystem.model.{Product, State, TaxRule}

class TaxCalculator(taxRules: List[TaxRule]) {
  def calculateTotalPrice(product: Product, state: State, year: Int): Double = {
    val applicableTaxRule = taxRules.find(rule =>
      rule.state == state && rule.product == product && rule.year == year
    )

    val taxAmount = applicableTaxRule.map(_.calculateTax).getOrElse(0.0)
    product.price + taxAmount
  }
}
