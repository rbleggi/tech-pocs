package com.rbleggi.logisticpricing.service

import com.rbleggi.logisticpricing.model.FreightInfo
import com.rbleggi.logisticpricing.model.TransportType
import com.rbleggi.logisticpricing.service.pricing.BoatBasePricing
import com.rbleggi.logisticpricing.service.pricing.FreightPricing
import com.rbleggi.logisticpricing.service.pricing.RailBasePricing
import com.rbleggi.logisticpricing.service.pricing.TruckBasePricing
import java.math.BigDecimal
import java.math.RoundingMode

class LogisticPricingService {
    fun baseFor(type: TransportType): FreightPricing = when (type) {
        TransportType.TRUCK -> TruckBasePricing()
        TransportType.RAIL -> RailBasePricing()
        TransportType.BOAT -> BoatBasePricing()
    }

    fun basePrice(info: FreightInfo): Double = quote(info, baseFor(info.transportType))

    fun quote(info: FreightInfo, pricing: FreightPricing): Double =
        BigDecimal(pricing.calculate(info)).setScale(2, RoundingMode.HALF_UP).toDouble()
}
