package com.rbleggi.guitarfactory

import org.scalatest.BeforeAndAfter
import org.scalatest.funsuite.AnyFunSuite

class GuitarFactorySpec extends AnyFunSuite with BeforeAndAfter {

  before {
    GuitarInventory.clearInventory()
  }

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

  test("Guitar builder with fluent interface") {
    val guitar = Guitar.builder()
      .guitarType("Acoustic")
      .model("Yamaha FG800")
      .specs("Spruce Top, Mahogany Back")
      .os("Custom OS 1.0")
      .build()

    assert(guitar.guitarType == "Acoustic")
    assert(guitar.model == "Yamaha FG800")
  }

  test("GuitarInventory adds guitars correctly") {
    val guitar = Guitar.builder()
      .guitarType("Bass")
      .model("Ibanez GSRM20")
      .specs("Poplar Body, Maple Neck")
      .os("Custom OS 3.0")
      .build()

    GuitarInventory.addGuitar(guitar, 2)
    assert(GuitarInventory.getQuantity(guitar) == 2)
  }

  test("GuitarInventory removes guitars correctly") {
    val guitar = Guitar.builder()
      .guitarType("Electric")
      .model("Gibson Les Paul")
      .specs("Mahogany Body, Rosewood Fretboard")
      .os("Custom OS 4.0")
      .build()

    GuitarInventory.addGuitar(guitar, 5)
    assert(GuitarInventory.getQuantity(guitar) == 5)

    GuitarInventory.removeGuitar(guitar, 2)
    assert(GuitarInventory.getQuantity(guitar) == 3)
  }

  test("GuitarInventory handles removing more guitars than available") {
    val guitar = Guitar.builder()
      .guitarType("Bass")
      .model("Fender Jazz Bass")
      .specs("Alder Body, Maple Neck")
      .os("Custom OS 5.0")
      .build()

    GuitarInventory.addGuitar(guitar, 2)
    GuitarInventory.removeGuitar(guitar, 1)
    GuitarInventory.removeGuitar(guitar, 2)
  }
}
