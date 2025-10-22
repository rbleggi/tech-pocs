package com.rbleggi.guitarfactory

import org.scalatest.funsuite.AnyFunSuite

class GuitarFactorySpec extends AnyFunSuite {
  test("Guitar builder creates correct Guitar instance") {
    val guitar = Guitar.builder()
      .guitarType("Electric")
      .model("Fender Stratocaster")
      .specs("Alder Body, Maple Neck")
      .os("Custom OS 2.0")
      .build()
    assert(guitar.guitarType == "Electric")
    assert(guitar.model == "Fender Stratocaster")
    assert(guitar.specs == "Alder Body, Maple Neck")
    assert(guitar.os == "Custom OS 2.0")
  }

  test("GuitarInventory add and remove guitars") {
    val guitar = Guitar.builder().guitarType("Bass").model("Ibanez GSRM20").specs("Poplar Body, Maple Neck").os("Custom OS 3.0").build()
    GuitarInventory.addGuitar(guitar, 2)
    GuitarInventory.removeGuitar(guitar, 1)
    GuitarInventory.removeGuitar(guitar, 2)
  }
}
