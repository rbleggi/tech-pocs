package com.rbleggi.guitarfactory

import com.rbleggi.guitarfactory.factory.GuitarFactory
import com.rbleggi.guitarfactory.inventory.GuitarInventory

@main def run(): Unit = {
  val guitar1 = GuitarFactory.createGuitar("acoustic", "Yamaha FG800", "Spruce Top, Mahogany Back", "Custom OS 1.0")
  val guitar2 = GuitarFactory.createGuitar("electric", "Fender Stratocaster", "Alder Body, Maple Neck", "Custom OS 2.0")
  val guitar3 = GuitarFactory.createGuitar("bass", "Ibanez GSRM20", "Poplar Body, Maple Neck", "Custom OS 3.0")

  GuitarInventory.addGuitar(guitar1, 5)
  GuitarInventory.addGuitar(guitar2, 3)
  GuitarInventory.addGuitar(guitar3, 2)

  GuitarInventory.listInventory()

  GuitarInventory.removeGuitar(guitar2, 1)
  GuitarInventory.removeGuitar(guitar3, 3)

  GuitarInventory.listInventory()
}