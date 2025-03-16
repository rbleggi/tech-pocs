package com.rbleggi.taxsystem.model

case class TaxRule(state: State, product: Product, year: Int, taxRate: Double):
  def calculateTax(): Double = product.price * (taxRate / 100)

