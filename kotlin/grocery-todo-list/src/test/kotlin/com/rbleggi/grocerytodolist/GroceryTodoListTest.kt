package com.rbleggi.grocerytodolist

import java.util.*
import kotlin.test.*

class GroceryItemTest {
    @Test
    fun `item is created with name and default values`() {
        val item = GroceryItem(name = "Milk")
        assertEquals("Milk", item.name)
        assertFalse(item.done)
        assertNotNull(item.id)
    }

    @Test
    fun `item can be created with done status`() {
        val item = GroceryItem(name = "Bread", done = true)
        assertEquals("Bread", item.name)
        assertTrue(item.done)
    }

    @Test
    fun `item done status can be changed`() {
        val item = GroceryItem(name = "Eggs")
        item.done = true
        assertTrue(item.done)
    }

    @Test
    fun `item id can be specified`() {
        val customId = UUID.randomUUID()
        val item = GroceryItem(id = customId, name = "Butter")
        assertEquals(customId, item.id)
    }
}

class GroceryListTest {
    @Test
    fun `addItem adds new item successfully`() {
        val list = GroceryList()
        val item = GroceryItem(name = "Milk")
        assertTrue(list.addItem(item))
        assertTrue(list.listAll().contains(item))
    }

    @Test
    fun `addItem rejects duplicate item id`() {
        val list = GroceryList()
        val item = GroceryItem(id = UUID.randomUUID(), name = "Milk")
        assertTrue(list.addItem(item))
        assertFalse(list.addItem(item))
    }

    @Test
    fun `removeItem removes existing item`() {
        val list = GroceryList()
        val item = GroceryItem(name = "Milk")
        list.addItem(item)
        val removed = list.removeItem(item.id)
        assertEquals(item, removed)
        assertFalse(list.listAll().contains(item))
    }

    @Test
    fun `removeItem returns null for non-existent item`() {
        val list = GroceryList()
        val nonExistentId = UUID.randomUUID()
        assertNull(list.removeItem(nonExistentId))
    }

    @Test
    fun `markAsDone marks item as done`() {
        val list = GroceryList()
        val item = GroceryItem(name = "Milk", done = false)
        list.addItem(item)
        assertTrue(list.markAsDone(item.id))
        assertTrue(item.done)
    }

    @Test
    fun `markAsDone returns false for already done item`() {
        val list = GroceryList()
        val item = GroceryItem(name = "Milk", done = true)
        list.addItem(item)
        assertFalse(list.markAsDone(item.id))
    }

    @Test
    fun `markAsDone returns false for non-existent item`() {
        val list = GroceryList()
        val nonExistentId = UUID.randomUUID()
        assertFalse(list.markAsDone(nonExistentId))
    }

    @Test
    fun `markAsUndone marks item as not done`() {
        val list = GroceryList()
        val item = GroceryItem(name = "Milk", done = true)
        list.addItem(item)
        assertTrue(list.markAsUndone(item.id))
        assertFalse(item.done)
    }

    @Test
    fun `markAsUndone returns false for already undone item`() {
        val list = GroceryList()
        val item = GroceryItem(name = "Milk", done = false)
        list.addItem(item)
        assertFalse(list.markAsUndone(item.id))
    }

    @Test
    fun `markAsUndone returns false for non-existent item`() {
        val list = GroceryList()
        val nonExistentId = UUID.randomUUID()
        assertFalse(list.markAsUndone(nonExistentId))
    }

    @Test
    fun `listAll returns all items`() {
        val list = GroceryList()
        val item1 = GroceryItem(name = "Milk")
        val item2 = GroceryItem(name = "Bread")
        val item3 = GroceryItem(name = "Eggs")
        list.addItem(item1)
        list.addItem(item2)
        list.addItem(item3)
        val allItems = list.listAll()
        assertEquals(3, allItems.size)
        assertTrue(allItems.contains(item1))
        assertTrue(allItems.contains(item2))
        assertTrue(allItems.contains(item3))
    }

    @Test
    fun `listAll returns empty list initially`() {
        val list = GroceryList()
        assertTrue(list.listAll().isEmpty())
    }
}

class AddItemCommandTest {
    @Test
    fun `execute adds item to grocery list`() {
        val list = GroceryList()
        val item = GroceryItem(name = "Milk")
        val command = AddItemCommand(list, item)
        assertTrue(command.execute())
        assertTrue(list.listAll().contains(item))
    }

    @Test
    fun `execute returns false for duplicate item`() {
        val list = GroceryList()
        val item = GroceryItem(name = "Milk")
        list.addItem(item)
        val command = AddItemCommand(list, item)
        assertFalse(command.execute())
    }

    @Test
    fun `undo removes added item`() {
        val list = GroceryList()
        val item = GroceryItem(name = "Milk")
        val command = AddItemCommand(list, item)
        command.execute()
        assertTrue(command.undo())
        assertFalse(list.listAll().contains(item))
    }

    @Test
    fun `undo returns false if item was not added`() {
        val list = GroceryList()
        val item = GroceryItem(name = "Milk")
        val command = AddItemCommand(list, item)
        assertFalse(command.undo())
    }
}

class RemoveItemCommandTest {
    @Test
    fun `execute removes item from grocery list`() {
        val list = GroceryList()
        val item = GroceryItem(name = "Milk")
        list.addItem(item)
        val command = RemoveItemCommand(list, item.id)
        assertTrue(command.execute())
        assertFalse(list.listAll().contains(item))
    }

    @Test
    fun `execute returns false for non-existent item`() {
        val list = GroceryList()
        val command = RemoveItemCommand(list, UUID.randomUUID())
        assertFalse(command.execute())
    }

    @Test
    fun `undo re-adds removed item`() {
        val list = GroceryList()
        val item = GroceryItem(name = "Milk")
        list.addItem(item)
        val command = RemoveItemCommand(list, item.id)
        command.execute()
        assertTrue(command.undo())
        assertTrue(list.listAll().contains(item))
    }

    @Test
    fun `undo returns false if item was not removed`() {
        val list = GroceryList()
        val command = RemoveItemCommand(list, UUID.randomUUID())
        command.execute()
        assertFalse(command.undo())
    }
}

class MarkAsDoneCommandTest {
    @Test
    fun `execute marks item as done`() {
        val list = GroceryList()
        val item = GroceryItem(name = "Milk", done = false)
        list.addItem(item)
        val command = MarkAsDoneCommand(list, item.id)
        assertTrue(command.execute())
        assertTrue(item.done)
    }

    @Test
    fun `execute returns false for already done item`() {
        val list = GroceryList()
        val item = GroceryItem(name = "Milk", done = true)
        list.addItem(item)
        val command = MarkAsDoneCommand(list, item.id)
        assertFalse(command.execute())
    }

    @Test
    fun `execute returns false for non-existent item`() {
        val list = GroceryList()
        val command = MarkAsDoneCommand(list, UUID.randomUUID())
        assertFalse(command.execute())
    }

    @Test
    fun `undo marks item as undone`() {
        val list = GroceryList()
        val item = GroceryItem(name = "Milk", done = false)
        list.addItem(item)
        val command = MarkAsDoneCommand(list, item.id)
        command.execute()
        assertTrue(command.undo())
        assertFalse(item.done)
    }

    @Test
    fun `undo returns false if item was not marked done`() {
        val list = GroceryList()
        val item = GroceryItem(name = "Milk", done = false)
        list.addItem(item)
        val command = MarkAsDoneCommand(list, item.id)
        assertFalse(command.undo())
    }
}

class ListAllItemsCommandTest {
    @Test
    fun `execute lists all items`() {
        val list = GroceryList()
        val item1 = GroceryItem(name = "Milk")
        val item2 = GroceryItem(name = "Bread")
        list.addItem(item1)
        list.addItem(item2)
        val command = ListAllItemsCommand(list)
        assertTrue(command.execute())
        assertEquals(2, command.result.size)
    }

    @Test
    fun `execute returns true for empty list`() {
        val list = GroceryList()
        val command = ListAllItemsCommand(list)
        assertTrue(command.execute())
        assertTrue(command.result.isEmpty())
    }

    @Test
    fun `undo returns false`() {
        val list = GroceryList()
        val command = ListAllItemsCommand(list)
        assertFalse(command.undo())
    }
}

class GroceryInvokerTest {
    @Test
    fun `execute runs command successfully`() {
        val list = GroceryList()
        val item = GroceryItem(name = "Milk")
        val invoker = GroceryInvoker()
        val command = AddItemCommand(list, item)
        invoker.execute(command)
        assertTrue(list.listAll().contains(item))
    }

    @Test
    fun `undo reverts last command`() {
        val list = GroceryList()
        val item = GroceryItem(name = "Milk")
        val invoker = GroceryInvoker()
        val command = AddItemCommand(list, item)
        invoker.execute(command)
        invoker.undo()
        assertFalse(list.listAll().contains(item))
    }

    @Test
    fun `redo reapplies undone command`() {
        val list = GroceryList()
        val item = GroceryItem(name = "Milk")
        val invoker = GroceryInvoker()
        val command = AddItemCommand(list, item)
        invoker.execute(command)
        invoker.undo()
        invoker.redo()
        assertTrue(list.listAll().contains(item))
    }

    @Test
    fun `ListAllItemsCommand does not add to undo stack`() {
        val list = GroceryList()
        val item = GroceryItem(name = "Milk")
        val invoker = GroceryInvoker()
        invoker.execute(AddItemCommand(list, item))
        invoker.execute(ListAllItemsCommand(list))
        invoker.undo()
        assertFalse(list.listAll().contains(item))
    }

    @Test
    fun `redo after new command clears redo stack`() {
        val list = GroceryList()
        val item1 = GroceryItem(name = "Milk")
        val item2 = GroceryItem(name = "Bread")
        val invoker = GroceryInvoker()
        invoker.execute(AddItemCommand(list, item1))
        invoker.undo()
        invoker.execute(AddItemCommand(list, item2))
        invoker.redo()
        assertEquals(1, list.listAll().size)
        assertTrue(list.listAll().any { it.name == "Bread" })
    }

    @Test
    fun `multiple undo operations work correctly`() {
        val list = GroceryList()
        val item1 = GroceryItem(name = "Milk")
        val item2 = GroceryItem(name = "Bread")
        val item3 = GroceryItem(name = "Eggs")
        val invoker = GroceryInvoker()
        invoker.execute(AddItemCommand(list, item1))
        invoker.execute(AddItemCommand(list, item2))
        invoker.execute(AddItemCommand(list, item3))
        invoker.undo()
        invoker.undo()
        assertEquals(1, list.listAll().size)
        assertTrue(list.listAll().contains(item1))
    }

    @Test
    fun `multiple redo operations work correctly`() {
        val list = GroceryList()
        val item1 = GroceryItem(name = "Milk")
        val item2 = GroceryItem(name = "Bread")
        val invoker = GroceryInvoker()
        invoker.execute(AddItemCommand(list, item1))
        invoker.execute(AddItemCommand(list, item2))
        invoker.undo()
        invoker.undo()
        invoker.redo()
        invoker.redo()
        assertEquals(2, list.listAll().size)
    }
}
