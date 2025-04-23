package com.rbleggi.grocerytodolist

import com.rbleggi.grocerytodolist.CommandInvoker.executeCommand
import com.rbleggi.grocerytodolist.GroceryList.*

case class GroceryItem(name: String, isDone: Boolean = false) {
  def markAsDone: GroceryItem = this.copy(isDone = true)

  def markAsUndone: GroceryItem = this.copy(isDone = false)

  override def toString: String = s"${if (isDone) "[X]" else "[ ]"} $name"
}

object GroceryList {
  private var items: List[GroceryItem] = List.empty

  def addItem(item: GroceryItem): Unit = {
    items = items :+ item
    println(s"Added: ${item.name}")
  }

  def removeItem(item: GroceryItem): Boolean = {
    val index = items.indexWhere(_.name == item.name)
    if (index >= 0) {
      items = items.patch(index, Nil, 1)
      println(s"Removed: ${item.name}")
      true
    } else {
      println(s"Item not found: ${item.name}")
      false
    }
  }

  def markAsDone(item: GroceryItem): Boolean = {
    val index = items.indexWhere(_.name == item.name)
    if (index >= 0) {
      val updatedItem = items(index).markAsDone
      items = items.updated(index, updatedItem)
      println(s"Marked as done: ${item.name}")
      true
    } else {
      println(s"Item not found: ${item.name}")
      false
    }
  }

  def markAsUndone(item: GroceryItem): Boolean = {
    val index = items.indexWhere(_.name == item.name)
    if (index >= 0) {
      val updatedItem = items(index).markAsUndone
      items = items.updated(index, updatedItem)
      println(s"Marked as not done: ${item.name}")
      true
    } else {
      println(s"Item not found: ${item.name}")
      false
    }
  }

  def listAll(): Unit = {
    println("\nGrocery List:")
    if (items.isEmpty) {
      println("No items in the list.")
    } else {
      items.foreach(item => println(item))
    }
  }

  def getItemByName(name: String): Option[GroceryItem] =
    items.find(_.name == name)
}

trait Command {
  def execute(): Boolean

  def undo(): Boolean
}

class AddItemCommand(item: GroceryItem) extends Command {
  override def execute(): Boolean = {
    addItem(item)
    true
  }

  override def undo(): Boolean = {
    removeItem(item)
  }
}

class RemoveItemCommand(item: GroceryItem) extends Command {
  private var removedItem: Option[GroceryItem] = None

  override def execute(): Boolean = {
    removedItem = GroceryList.getItemByName(item.name)
    removeItem(item)
  }

  override def undo(): Boolean = {
    removedItem match {
      case Some(item) =>
        addItem(item)
        true
      case None =>
        println(s"Cannot undo removal: ${item.name}")
        false
    }
  }
}

class MarkAsDoneCommand(item: GroceryItem) extends Command {
  override def execute(): Boolean = {
    markAsDone(item)
  }

  override def undo(): Boolean = {
    markAsUndone(item)
  }
}

object CommandInvoker {
  private var history: List[Command] = List.empty
  private var redoStack: List[Command] = List.empty

  def executeCommand(command: Command): Boolean = {
    val result = command.execute()
    if (result) {
      history = command :: history
      redoStack = List.empty
    }
    result
  }

  def undo(): Boolean =
    history match {
      case cmd :: rest =>
        val result = cmd.undo()
        if (result) {
          history = rest
          redoStack = cmd :: redoStack
          println("Undo successful")
        } else {
          println("Undo failed")
        }
        result
      case Nil =>
        println("Nothing to undo")
        false
    }

  def redo(): Boolean =
    redoStack match {
      case cmd :: rest =>
        val result = cmd.execute()
        if (result) {
          redoStack = rest
          history = cmd :: history
          println("Redo successful")
        } else {
          println("Redo failed")
        }
        result
      case Nil =>
        println("Nothing to redo")
        false
    }
}

@main def run(): Unit = {
  println("\nWelcome to Grocery TODO List!")
  println("--------------------------------")

  val milkItem = GroceryItem("Milk")
  val breadItem = GroceryItem("Bread")
  val eggsItem = GroceryItem("Eggs")

  executeCommand(new AddItemCommand(milkItem))
  executeCommand(new AddItemCommand(breadItem))
  executeCommand(new AddItemCommand(eggsItem))

  listAll()
  executeCommand(new MarkAsDoneCommand(milkItem))
  listAll()
  executeCommand(new RemoveItemCommand(breadItem))
  listAll()
  CommandInvoker.undo()
  listAll()
  CommandInvoker.undo()
  listAll()
  CommandInvoker.redo()
  listAll()
}