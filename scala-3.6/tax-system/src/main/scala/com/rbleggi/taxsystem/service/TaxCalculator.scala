package com.rbleggi.taxsystem.service

import com.rbleggi.taxsystem.model.{Product, State, TaxRule}
import com.rbleggi.taxsystem.specification.{ProductSpecification, StateSpecification, YearSpecification}

class TaxCalculator(rules: List[TaxRule]):

  def calculateTotalPrice(product: Product, state: State, year: Int): Double =
    val productSpec = ProductSpecification(product)
    val stateSpec = StateSpecification(state)
    val yearSpec = YearSpecification(year)

    val combinedSpec = productSpec.and(stateSpec).and(yearSpec)

    rules
      .filter(combinedSpec.matches)
      .map(_.calculateTax())
      .headOption
      .map(tax => product.price + tax)
      .getOrElse(product.price)
