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

class GuitarInventory {
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

    fun getQuantity(guitar: Guitar): Int = inventory.getOrDefault(guitar, 0)

    fun contains(guitar: Guitar): Boolean = inventory.containsKey(guitar)

    fun isEmpty(): Boolean = inventory.isEmpty()

    fun listInventory(): List<Pair<Guitar, Int>> = inventory.toList()
}

fun main() {
    println("Guitar Factory POC")
}
