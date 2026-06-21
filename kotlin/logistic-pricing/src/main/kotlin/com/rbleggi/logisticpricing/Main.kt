package com.rbleggi.logisticpricing

import com.rbleggi.logisticpricing.model.FreightInfo
import com.rbleggi.logisticpricing.model.TransportType
import com.rbleggi.logisticpricing.service.LogisticPricingService
import com.rbleggi.logisticpricing.service.decorator.DistanceDiscount
import com.rbleggi.logisticpricing.service.decorator.FuelSurcharge
import com.rbleggi.logisticpricing.service.decorator.Insurance

fun main() {
    println("Logistic Pricing")

    val service = LogisticPricingService()
    val info = FreightInfo(15.0, 6.0, 500.0, TransportType.TRUCK)

    println("\n== Base price per transport ==")
    TransportType.entries.forEach { type ->
        println("$type: ${service.basePrice(info.copy(transportType = type))}")
    }

    println("\n== Decorator (stacked surcharges over the base price) ==")
    val base = service.baseFor(TransportType.TRUCK)
    val withFuel = FuelSurcharge(base, 12.0)
    val withInsurance = Insurance(withFuel, 30.0)
    val full = DistanceDiscount(withInsurance, threshold = 300.0, percent = 5.0)

    println("base:                ${service.quote(info, base)}")
    println("+ fuel 12%:          ${service.quote(info, withFuel)}")
    println("+ insurance R\$30:    ${service.quote(info, withInsurance)}")
    println("+ distance disc 5%:  ${service.quote(info, full)}")
}
