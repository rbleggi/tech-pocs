package com.rbleggi.taxsystem

import com.rbleggi.taxsystem.model.TaxRule
import com.rbleggi.taxsystem.service.TaxSystemService
import com.rbleggi.taxsystem.service.strategy.CappedTax
import com.rbleggi.taxsystem.service.strategy.FlatTax
import com.rbleggi.taxsystem.service.strategy.PercentageTax
import com.rbleggi.taxsystem.service.templatemethod.CappedBill
import com.rbleggi.taxsystem.service.templatemethod.FlatBill
import com.rbleggi.taxsystem.service.templatemethod.PercentageBill
import com.rbleggi.taxsystem.service.templatemethod.TaxTemplateMethod

fun main() {
    println("Tax System")

    println("\n== Strategy (composition: TaxRule + TaxStrategy) ==")
    val rules = listOf(
        TaxRule("Electronics", "SP", PercentageTax(18.0)),
        TaxRule("Refrigerator", "RJ", CappedTax(20.0, 500.0)),
        TaxRule("Electronics", "MG", PercentageTax(12.0)),
        TaxRule("Electronics", "RS", FlatTax(50.0))
    )
    val service = TaxSystemService(rules)
    println("Electronics in SP (18% percentage): ${service.totalPrice("Electronics", 1000.0, "SP")}")
    println("Refrigerator in RJ (20% capped at R\$500): ${service.totalPrice("Refrigerator", 5000.0, "RJ")}")
    println("Electronics in MG (12% percentage): ${service.totalPrice("Electronics", 1000.0, "MG")}")
    println("Electronics in RS (flat R\$50): ${service.totalPrice("Electronics", 1000.0, "RS")}")

    println("\n== Template Method (inheritance: TaxTemplateMethod subclasses) ==")
    val bills: Map<String, TaxTemplateMethod> = mapOf(
        "SP" to PercentageBill(18.0),
        "RJ" to CappedBill(20.0, 500.0),
        "MG" to PercentageBill(12.0),
        "RS" to FlatBill(50.0)
    )
    println("Electronics in SP (18% percentage): ${service.totalPriceByTemplate(bills.getValue("SP"), 1000.0)}")
    println("Refrigerator in RJ (20% capped at R\$500): ${service.totalPriceByTemplate(bills.getValue("RJ"), 5000.0)}")
    println("Electronics in MG (12% percentage): ${service.totalPriceByTemplate(bills.getValue("MG"), 1000.0)}")
    println("Electronics in RS (flat R\$50): ${service.totalPriceByTemplate(bills.getValue("RS"), 1000.0)}")
}
