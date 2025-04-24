package com.rbleggi.grocerytodolist

import com.rbleggi.grocerytodolist.CommandInvoker.executeCommand

@main def run(): Unit = {
  println("\nWelcome to Grocery TODO List!")
  println("--------------------------------")

  val manager = new GroceryList()

  val milkItem = GroceryItem("Milk")
  val breadItem = GroceryItem("Bread")
  val eggsItem = GroceryItem("Eggs")

  executeCommand(new AddItemCommand(milkItem, manager))
  executeCommand(new AddItemCommand(breadItem, manager))
  executeCommand(new AddItemCommand(eggsItem, manager))

  manager.listAll()
  executeCommand(new MarkAsDoneCommand(milkItem, manager))
  manager.listAll()
  executeCommand(new RemoveItemCommand(breadItem, manager))
  manager.listAll()
  CommandInvoker.undo()
  manager.listAll()
  CommandInvoker.undo()
  manager.listAll()
  CommandInvoker.redo()
  manager.listAll()
}