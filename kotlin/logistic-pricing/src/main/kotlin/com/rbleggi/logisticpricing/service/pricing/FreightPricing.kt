package com.rbleggi.logisticpricing.service.pricing

import com.rbleggi.logisticpricing.model.FreightInfo

interface FreightPricing {
    fun calculate(info: FreightInfo): Double
}

class TruckBasePricing : FreightPricing {
    override fun calculate(info: FreightInfo): Double =
        info.volume * 0.8 + info.size * 0.5 + info.distance * 1.5
}

class RailBasePricing : FreightPricing {
    override fun calculate(info: FreightInfo): Double =
        info.volume * 0.6 + info.size * 0.4 + info.distance * 1.2
}

class BoatBasePricing : FreightPricing {
    override fun calculate(info: FreightInfo): Double =
        info.volume * 0.4 + info.size * 0.3 + info.distance * 1.0
}
