package com.rbleggi.restaurantqueue


interface DishCommand {
    val name: String
    fun prepTime(): Int
    fun execute()
}

data class BurgerCommand(override val name: String = "Burger") : DishCommand {
    override fun prepTime() = 10
    override fun execute() {
        println("Preparing $name. It will take ${prepTime()} minutes.")
    }
}

data class PastaCommand(override val name: String = "Pasta") : DishCommand {
    override fun prepTime() = 15
    override fun execute() {
        println("Preparing $name. It will take ${prepTime()} minutes.")
    }
}

data class SaladCommand(override val name: String = "Salad") : DishCommand {
    override fun prepTime() = 5
    override fun execute() {
        println("Preparing $name. It will take ${prepTime()} minutes.")
    }
}

class KitchenQueue {
    private val commands = mutableListOf<DishCommand>()

    fun addCommand(cmd: DishCommand) {
        commands.add(cmd)
        println("Added ${cmd.name} to the queue.")
    }

    fun runQueue() {
        var totalTime = 0
        println("\n--- Running Kitchen Queue ---")
        for (cmd in commands) {
            cmd.execute()
            totalTime += cmd.prepTime()
        }
        println("Total preparation time: $totalTime minutes.")
    }
}

fun main() {
    val kitchenQueue = KitchenQueue()
    kitchenQueue.addCommand(BurgerCommand())
    kitchenQueue.addCommand(PastaCommand())
    kitchenQueue.addCommand(SaladCommand())
    kitchenQueue.runQueue()
}