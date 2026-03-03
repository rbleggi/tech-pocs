package com.rbleggi.grocerytodolist

import scala.collection.mutable

case class GroceryItem(name: String, isDone: Boolean = false):
  def markAsDone: GroceryItem = this.copy(isDone = true)
  def markAsUndone: GroceryItem = this.copy(isDone = false)
  override def toString: String = s"${if isDone then "[X]" else "[ ]"} $name"

trait Command:
  def execute(items: List[GroceryItem]): List[GroceryItem]
  def undo(items: List[GroceryItem]): List[GroceryItem]

class AddItemCommand(item: GroceryItem) extends Command:
  override def execute(items: List[GroceryItem]): List[GroceryItem] =
    if !items.exists(_.name == item.name) then
      println(s"Added: ${item.name}")
      items :+ item
    else
      println(s"Item already exists: ${item.name}")
      items

  override def undo(items: List[GroceryItem]): List[GroceryItem] =
    val index = items.indexWhere(_.name == item.name)
    if index >= 0 then
      println(s"Undone addition: ${item.name}")
      items.patch(index, Nil, 1)
    else
      println(s"Item not found for undo: ${item.name}")
      items

class RemoveItemCommand(item: GroceryItem) extends Command:
  private var removedItem: Option[GroceryItem] = None

  override def execute(items: List[GroceryItem]): List[GroceryItem] =
    val index = items.indexWhere(_.name == item.name)
    if index >= 0 then
      removedItem = Some(items(index))
      println(s"Removed: ${item.name}")
      items.patch(index, Nil, 1)
    else
      println(s"Item not found: ${item.name}")
      items

  override def undo(items: List[GroceryItem]): List[GroceryItem] =
    removedItem match
      case Some(i) =>
        println(s"Undone removal: ${i.name}")
        items :+ i
      case None =>
        println(s"Cannot undo removal: ${item.name}")
        items

class MarkAsDoneCommand(item: GroceryItem) extends Command:
  private var previousState: Option[GroceryItem] = None

  override def execute(items: List[GroceryItem]): List[GroceryItem] =
    val index = items.indexWhere(_.name == item.name)
    if index >= 0 then
      previousState = Some(items(index))
      val updatedItem = items(index).markAsDone
      println(s"Marked as done: ${item.name}")
      items.updated(index, updatedItem)
    else
      println(s"Item not found: ${item.name}")
      items

  override def undo(items: List[GroceryItem]): List[GroceryItem] =
    previousState match
      case Some(prevItem) =>
        val index = items.indexWhere(_.name == item.name)
        if index >= 0 then
          println(s"Undone mark as done: ${item.name}")
          items.updated(index, prevItem)
        else
          println(s"Item not found for undo: ${item.name}")
          items
      case None =>
        println(s"Cannot undo: ${item.name}")
        items

class GroceryManager:
  private var items: List[GroceryItem] = List.empty

  def getItems: List[GroceryItem] = items

  def applyChanges(newItems: List[GroceryItem]): Unit =
    items = newItems

  def listAll(): Unit =
    println("\nGrocery List:")
    if items.isEmpty then println("No items in the list.")
    else items.foreach(item => println(item))

object CommandInvoker:
  private val history = mutable.Stack[Command]()
  private val redoStack = mutable.Stack[Command]()

  def executeCommand(command: Command, items: List[GroceryItem]): List[GroceryItem] =
    val result = command.execute(items)
    history.push(command)
    redoStack.clear()
    result

  def undo(items: List[GroceryItem]): List[GroceryItem] =
    if history.nonEmpty then
      val command = history.pop()
      val result = command.undo(items)
      redoStack.push(command)
      println("Undo successful")
      result
    else
      println("Nothing to undo")
      items

  def redo(items: List[GroceryItem]): List[GroceryItem] =
    if redoStack.nonEmpty then
      val command = redoStack.pop()
      val result = command.execute(items)
      history.push(command)
      println("Redo successful")
      result
    else
      println("Nothing to redo")
      items

@main def run(): Unit =
  println("Grocery Todo List")
