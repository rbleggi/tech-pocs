package com.rbleggi.grocerytodolist

class GroceryTodoListSpec {
  test("AddItemCommand adds a new item") {
    val item = GroceryItem("Milk")
    val cmd = new AddItemCommand(item)
    val result = cmd.execute(List())
    assert(result.contains(item))
  }

  test("AddItemCommand does not add duplicate items") {
    val item = GroceryItem("Milk")
    val cmd = new AddItemCommand(item)
    val result = cmd.execute(List(item))
    assert(result.count(_.name == "Milk") == 1)
  }

  test("RemoveItemCommand removes an item") {
    val item = GroceryItem("Milk")
    val cmd = new RemoveItemCommand(item)
    val result = cmd.execute(List(item))
    assert(!result.contains(item))
  }

  test("GroceryItem markAsDone and markAsUndone") {
    val item = GroceryItem("Milk")
    val done = item.markAsDone
    assert(done.isDone)
    val undone = done.markAsUndone
    assert(!undone.isDone)
  }

  test("GroceryManager applyChanges and getItems") {
    val manager = new GroceryManager()
    val item = GroceryItem("Milk")
    manager.applyChanges(List(item))
    assert(manager.getItems == List(item))
  }
}
