package com.rbleggi.guitarfactory.inventory

import com.rbleggi.guitarfactory.model.Guitar

object GuitarInventory {
  private var inventory: Map[Guitar, Int] = Map()

  def addGuitar(guitar: Guitar, quantity: Int = 1): Unit = {
    inventory = inventory + (guitar -> (inventory.getOrElse(guitar, 0) + quantity))
    println(s"Added $quantity ${guitar.model} to inventory.")
  }

  def removeGuitar(guitar: Guitar, quantity: Int = 1): Unit = {
    inventory.get(guitar) match {
      case Some(count) if count >= quantity =>
        inventory = inventory + (guitar -> (count - quantity))
        println(s"Removed $quantity ${guitar.model} from inventory.")
      case Some(_) =>
        println(s"Not enough stock for ${guitar.model}.")
      case None =>
        println(s"Guitar ${guitar.model} not found in inventory.")
    }
  }

  def listInventory(): Unit = {
    println("\nCurrent Guitar Inventory:")
    if (inventory.isEmpty) {
      println("No guitars in stock.")
    } else {
      inventory.foreach { case (guitar, count) => println(s"${guitar.toString}, Quantity: $count") }
    }
  }
}