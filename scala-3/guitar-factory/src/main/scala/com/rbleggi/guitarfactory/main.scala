package com.rbleggi.guitarfactory

case class Guitar private(guitarType: String, model: String, specs: String, os: String) {
  override def toString: String =
    s"Type: $guitarType, Model: $model, Specs: $specs, OS: $os"
}

object Guitar {
  def builder(): Builder = new Builder()

  class Builder private[Guitar]() {
    private var guitarType: String = ""
    private var model: String = ""
    private var specs: String = ""
    private var os: String = ""

    def guitarType(guitarType: String): Builder = {
      this.guitarType = guitarType
      this
    }

    def model(model: String): Builder = {
      this.model = model
      this
    }

    def specs(specs: String): Builder = {
      this.specs = specs
      this
    }

    def os(os: String): Builder = {
      this.os = os
      this
    }

    def build(): Guitar = Guitar(guitarType, model, specs, os)
  }
}

object GuitarInventory {
  private var inventory: Map[Guitar, Int] = Map()

  def addGuitar(guitar: Guitar, quantity: Int = 1): Unit = {
    inventory = inventory + (guitar -> (inventory.getOrElse(guitar, 0) + quantity))
    println(s"Added $quantity ${guitar.model} (${guitar.guitarType}) to inventory.")
  }

  def removeGuitar(guitar: Guitar, quantity: Int = 1): Unit = {
    inventory.get(guitar) match {
      case Some(count) if count >= quantity =>
        inventory = inventory + (guitar -> (count - quantity))
        println(s"Removed $quantity ${guitar.model} (${guitar.guitarType}) from inventory.")
      case Some(_) =>
        println(s"Not enough stock for ${guitar.model} (${guitar.guitarType}).")
      case None =>
        println(s"Guitar ${guitar.model} (${guitar.guitarType}) not found in inventory.")
    }
  }

  def getQuantity(guitar: Guitar): Int = {
    inventory.getOrElse(guitar, 0)
  }

  def clearInventory(): Unit = {
    inventory = Map()
  }

  def listInventory(): Unit = {
    println("\nCurrent Guitar Inventory:")
    if (inventory.isEmpty) {
      println("No guitars in stock.")
    } else {
      inventory.foreach { case (guitar, count) =>
        println(s"${guitar.toString}, Quantity: $count")
      }
    }
  }
}

@main def run(): Unit =
  println("Guitar Factory")