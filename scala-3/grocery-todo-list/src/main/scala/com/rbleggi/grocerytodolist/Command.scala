package com.rbleggi.grocerytodolist

trait Command {
  def execute(): Boolean

  def undo(): Boolean
}

class AddItemCommand(item: GroceryItem, manager: GroceryList) extends Command {
  override def execute(): Boolean = {
    val items = manager.getItems
    if (!items.exists(_.name == item.name)) {
      manager.setItems(items :+ item)
      println(s"Added: ${item.name}")
      true
    } else {
      println(s"Item already exists: ${item.name}")
      false
    }
  }

  override def undo(): Boolean = {
    val items = manager.getItems
    val index = items.indexWhere(_.name == item.name)
    if (index >= 0) {
      manager.setItems(items.patch(index, Nil, 1))
      println(s"Undone addition: ${item.name}")
      true
    } else {
      println(s"Item not found for undo: ${item.name}")
      false
    }
  }
}

class RemoveItemCommand(item: GroceryItem, manager: GroceryList) extends Command {
  private var removedItem: Option[GroceryItem] = None

  override def execute(): Boolean = {
    val items = manager.getItems
    val index = items.indexWhere(_.name == item.name)
    if (index >= 0) {
      removedItem = Some(items(index))
      manager.setItems(items.patch(index, Nil, 1))
      println(s"Removed: ${item.name}")
      true
    } else {
      println(s"Item not found: ${item.name}")
      false
    }
  }

  override def undo(): Boolean =
    removedItem match {
      case Some(item) =>
        val items = manager.getItems
        manager.setItems(items :+ item)
        println(s"Undone removal: ${item.name}")
        true
      case None =>
        println(s"Cannot undo removal: ${item.name}")
        false
    }
}

class MarkAsDoneCommand(item: GroceryItem, manager: GroceryList) extends Command {
  private var previousState: Option[GroceryItem] = None

  override def execute(): Boolean = {
    val items = manager.getItems
    val index = items.indexWhere(_.name == item.name)
    if (index >= 0) {
      previousState = Some(items(index))
      val updatedItem = items(index).markAsDone
      manager.setItems(items.updated(index, updatedItem))
      println(s"Marked as done: ${item.name}")
      true
    } else {
      println(s"Item not found: ${item.name}")
      false
    }
  }

  override def undo(): Boolean = {
    previousState match {
      case Some(prevItem) =>
        val items = manager.getItems
        val index = items.indexWhere(_.name == item.name)
        if (index >= 0) {
          manager.setItems(items.updated(index, prevItem))
          println(s"Undone mark as done: ${item.name}")
          true
        } else {
          println(s"Item not found for undo: ${item.name}")
          false
        }
      case None =>
        println(s"Cannot undo: ${item.name}")
        false
    }
  }
}
