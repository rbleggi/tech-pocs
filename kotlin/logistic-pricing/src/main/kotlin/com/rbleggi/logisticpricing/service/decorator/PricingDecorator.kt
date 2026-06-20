package com.rbleggi.logisticpricing.service.decorator

import com.rbleggi.logisticpricing.model.FreightInfo
import com.rbleggi.logisticpricing.service.pricing.FreightPricing
import kotlin.random.Random

abstract class PricingDecorator(protected val inner: FreightPricing) : FreightPricing

class FuelSurcharge(inner: FreightPricing, private val percent: Double) : PricingDecorator(inner) {
    override fun calculate(info: FreightInfo): Double = inner.calculate(info) * (1.0 + percent / 100)
}

class Insurance(inner: FreightPricing, private val fee: Double) : PricingDecorator(inner) {
    override fun calculate(info: FreightInfo): Double = inner.calculate(info) + fee
}

class DistanceDiscount(
    inner: FreightPricing,
    private val threshold: Double,
    private val percent: Double
) : PricingDecorator(inner) {
    override fun calculate(info: FreightInfo): Double {
        val price = inner.calculate(info)
        return if (info.distance >= threshold) price * (1.0 - percent / 100) else price
    }
}

class DynamicVariation(
    inner: FreightPricing,
    private val spread: Double,
    private val random: Random = Random.Default
) : PricingDecorator(inner) {
    override fun calculate(info: FreightInfo): Double = inner.calculate(info) * (1.0 + random.nextDouble(-spread, spread))
}
