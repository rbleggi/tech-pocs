package com.rbleggi.logisticpricing

import java.math.BigDecimal
import java.math.RoundingMode
import kotlin.random.Random

enum class TransportType {
    TRUCK, RAIL, BOAT
}

data class FreightInfo(
    val volume: Double,
    val size: Double,
    val distance: Double,
    val transportType: TransportType
)

interface PricingStrategy {
    fun calculate(info: FreightInfo): Double
}

class TruckPricingStrategy : PricingStrategy {
    override fun calculate(info: FreightInfo): Double {
        val base = 1.5
        val price = (info.volume * 0.8 + info.size * 0.5 + info.distance * base) * getDynamicFactor()
        return BigDecimal(price).setScale(2, RoundingMode.HALF_UP).toDouble()
    }

    private fun getDynamicFactor(): Double = 1.0 + Random.nextDouble(-0.1, 0.1)
}

class RailPricingStrategy : PricingStrategy {
    override fun calculate(info: FreightInfo): Double {
        val base = 1.2
        val price = (info.volume * 0.6 + info.size * 0.4 + info.distance * base) * getDynamicFactor()
        return BigDecimal(price).setScale(2, RoundingMode.HALF_UP).toDouble()
    }

    private fun getDynamicFactor(): Double = 1.0 + Random.nextDouble(-0.15, 0.15)
}

class BoatPricingStrategy : PricingStrategy {
    override fun calculate(info: FreightInfo): Double {
        val base = 1.0
        val price = (info.volume * 0.4 + info.size * 0.3 + info.distance * base) * getDynamicFactor()
        return BigDecimal(price).setScale(2, RoundingMode.HALF_UP).toDouble()
    }

    private fun getDynamicFactor(): Double = 1.0 + Random.nextDouble(-0.2, 0.2)
}

class FreightCalculator(private val strategy: PricingStrategy) {
    fun calculate(info: FreightInfo): Double = strategy.calculate(info)
}

object PricingStrategySelector {
    fun forTransportType(t: TransportType): PricingStrategy = when (t) {
        TransportType.TRUCK -> TruckPricingStrategy()
        TransportType.RAIL -> RailPricingStrategy()
        TransportType.BOAT -> BoatPricingStrategy()
    }
}

fun main() {
    println("Logistic Pricing")
}
