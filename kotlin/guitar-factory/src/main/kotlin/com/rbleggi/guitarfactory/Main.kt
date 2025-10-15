package com.rbleggi.guitarfactory

class Guitar private constructor(
    val type: String,
    val model: String,
    val specs: String,
    val os: String
) {
    override fun toString(): String {
        return "Guitar(type='$type', model='$model', specs='$specs', os='$os')"
    }

    class Builder {
        private var type: String = ""
        private var model: String = ""
        private var specs: String = ""
        private var os: String = ""

        fun type(type: String) = apply { this.type = type }
        fun model(model: String) = apply { this.model = model }
        fun specs(specs: String) = apply { this.specs = specs }
        fun os(os: String) = apply { this.os = os }

        fun build(): Guitar {
            return Guitar(type, model, specs, os)
        }
    }
}

object GuitarInventory {
    private val inventory = mutableMapOf<Guitar, Int>()

    fun addGuitar(guitar: Guitar, quantity: Int = 1) {
        inventory[guitar] = inventory.getOrDefault(guitar, 0) + quantity
    }

    fun removeGuitar(guitar: Guitar, quantity: Int = 1) {
        val current = inventory.getOrDefault(guitar, 0)
        if (current <= quantity) {
            inventory.remove(guitar)
        } else {
            inventory[guitar] = current - quantity
        }
    }

    fun listInventory() {
        if (inventory.isEmpty()) {
            println("Inventory is empty.")
        } else {
            println("Current Inventory:")
            inventory.forEach { (guitar, qty) ->
                println("$guitar | Quantity: $qty")
            }
        }
    }
}

fun main() {
    val strat = Guitar.Builder()
        .type("Electric")
        .model("Stratocaster")
        .specs("Alder body, Maple neck, 3 single-coil pickups")
        .os("CustomOS v1.0")
        .build()

    val lesPaul = Guitar.Builder()
        .type("Electric")
        .model("Les Paul")
        .specs("Mahogany body, Maple top, 2 humbuckers")
        .os("CustomOS v2.0")
        .build()

    GuitarInventory.addGuitar(strat, 5)
    GuitarInventory.addGuitar(lesPaul, 3)
    GuitarInventory.listInventory()

    GuitarInventory.removeGuitar(strat, 2)
    println("\nAfter removing 2 Stratocasters:")
    GuitarInventory.listInventory()
}
