package com.rbleggi.logisticpricing

import org.scalatest.funsuite.AnyFunSuite

class LogisticPricingSpec extends AnyFunSuite {
  test("TruckPricingStrategy calculates price in expected range") {
    val info = FreightInfo(10, 5, 100, TransportType.Truck)
    val price = new TruckPricingStrategy().calculate(info)
    assert(price > 135 && price < 190) // Further widened for dynamic factor
  }

  test("RailPricingStrategy calculates price in expected range") {
    val info = FreightInfo(20, 8, 300, TransportType.Rail)
    val price = new RailPricingStrategy().calculate(info)
    assert(price > 330 && price < 470)
  }

  test("BoatPricingStrategy calculates price in expected range") {
    val info = FreightInfo(15, 6, 500, TransportType.Boat)
    val price = new BoatPricingStrategy().calculate(info)
    assert(price > 380 && price < 670)
  }

  test("PricingStrategySelector returns correct strategy") {
    assert(PricingStrategySelector.forTransportType(TransportType.Truck).isInstanceOf[TruckPricingStrategy])
    assert(PricingStrategySelector.forTransportType(TransportType.Rail).isInstanceOf[RailPricingStrategy])
    assert(PricingStrategySelector.forTransportType(TransportType.Boat).isInstanceOf[BoatPricingStrategy])
  }
}
