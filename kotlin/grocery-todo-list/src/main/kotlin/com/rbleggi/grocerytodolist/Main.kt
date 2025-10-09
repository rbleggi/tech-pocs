package com.rbleggi.grocerytodolist

import java.util.*

data class GroceryItem(val id: UUID = UUID.randomUUID(), val name: String, var done: Boolean = false)

class GroceryList {
    private val items = mutableMapOf<UUID, GroceryItem>()

    fun addItem(item: GroceryItem): Boolean {
        if (items.containsKey(item.id)) return false
        items[item.id] = item
        println("Added item: ${item.name}")
        return true
    }

    fun removeItem(itemId: UUID): GroceryItem? {
        val removed = items.remove(itemId)
        if (removed != null) println("Removed item: ${removed.name}")
        return removed
    }

    fun markAsDone(itemId: UUID): Boolean {
        val item = items[itemId] ?: return false
        if (!item.done) {
            item.done = true
            println("Marked as done: ${item.name}")
            return true
        }
        return false
    }

    fun markAsUndone(itemId: UUID): Boolean {
        val item = items[itemId] ?: return false
        if (item.done) {
            item.done = false
            println("Marked as not done: ${item.name}")
            return true
        }
        return false
    }

    fun listAll(): List<GroceryItem> = items.values.toList()
}

interface Command {
    fun execute(): Boolean
    fun undo(): Boolean
}

class AddItemCommand(private val groceryList: GroceryList, private val item: GroceryItem) : Command {
    override fun execute(): Boolean = groceryList.addItem(item)
    override fun undo(): Boolean = groceryList.removeItem(item.id) != null
}

class RemoveItemCommand(private val groceryList: GroceryList, private val itemId: UUID) : Command {
    private var removedItem: GroceryItem? = null
    override fun execute(): Boolean {
        removedItem = groceryList.removeItem(itemId)
        return removedItem != null
    }
    override fun undo(): Boolean = removedItem?.let { groceryList.addItem(it) } ?: false
}

class MarkAsDoneCommand(private val groceryList: GroceryList, private val itemId: UUID) : Command {
    override fun execute(): Boolean = groceryList.markAsDone(itemId)
    override fun undo(): Boolean = groceryList.markAsUndone(itemId)
}

class ListAllItemsCommand(private val groceryList: GroceryList) : Command {
    var result: List<GroceryItem> = emptyList()
    override fun execute(): Boolean {
        result = groceryList.listAll()
        println("Current Grocery List:")
        result.forEach { println("- ${it.name} [${if (it.done) "Done" else "Not Done"}]") }
        return true
    }
    override fun undo(): Boolean = false
}

class GroceryInvoker {
    private val undoStack = Stack<Command>()
    private val redoStack = Stack<Command>()

    fun execute(command: Command) {
        if (command.execute()) {
            if (command !is ListAllItemsCommand) {
                undoStack.push(command)
                redoStack.clear()
            }
        }
    }

    fun undo() {
        if (undoStack.isNotEmpty()) {
            val command = undoStack.pop()
            if (command.undo()) {
                redoStack.push(command)
                println("Undo successful.")
            } else {
                println("Nothing to undo.")
            }
        } else {
            println("Undo stack is empty.")
        }
    }

    fun redo() {
        if (redoStack.isNotEmpty()) {
            val command = redoStack.pop()
            if (command.execute()) {
                undoStack.push(command)
                println("Redo successful.")
            } else {
                println("Nothing to redo.")
            }
        } else {
            println("Redo stack is empty.")
        }
    }
}

fun main() {
    val groceryList = GroceryList()
    val invoker = GroceryInvoker()

    val item1 = GroceryItem(name = "Milk")
    val item2 = GroceryItem(name = "Bread")
    val item3 = GroceryItem(name = "Eggs")

    invoker.execute(AddItemCommand(groceryList, item1))
    invoker.execute(AddItemCommand(groceryList, item2))
    invoker.execute(AddItemCommand(groceryList, item3))
    invoker.execute(ListAllItemsCommand(groceryList))

    invoker.execute(MarkAsDoneCommand(groceryList, item2.id))
    invoker.execute(ListAllItemsCommand(groceryList))

    invoker.undo() // Undo mark as done
    invoker.execute(ListAllItemsCommand(groceryList))

    invoker.redo() // Redo mark as done
    invoker.execute(ListAllItemsCommand(groceryList))

    invoker.execute(RemoveItemCommand(groceryList, item1.id))
    invoker.execute(ListAllItemsCommand(groceryList))

    invoker.undo() // Undo remove
    invoker.execute(ListAllItemsCommand(groceryList))
}
