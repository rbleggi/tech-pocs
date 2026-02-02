package com.rbleggi.grocerytodolist;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GroceryTodoListTest {
    @Test
    void addItemCommandAddsNewItem() {
        GroceryItem item = new GroceryItem("Milk");
        Command cmd = new AddItemCommand(item);
        List<GroceryItem> result = cmd.execute(List.of());
        assertTrue(result.contains(item));
    }

    @Test
    void addItemCommandDoesNotAddDuplicateItems() {
        GroceryItem item = new GroceryItem("Milk");
        Command cmd = new AddItemCommand(item);
        List<GroceryItem> result = cmd.execute(List.of(item));
        assertEquals(1, result.stream().filter(i -> i.getName().equals("Milk")).count());
    }

    @Test
    void removeItemCommandRemovesItem() {
        GroceryItem item = new GroceryItem("Milk");
        Command cmd = new RemoveItemCommand(item);
        List<GroceryItem> result = cmd.execute(List.of(item));
        assertFalse(result.contains(item));
    }

    @Test
    void groceryItemMarkAsDoneAndMarkAsUndone() {
        GroceryItem item = new GroceryItem("Milk");
        GroceryItem done = item.markAsDone();
        assertTrue(done.isDone());
        GroceryItem undone = done.markAsUndone();
        assertFalse(undone.isDone());
    }

    @Test
    void groceryManagerApplyChangesAndGetItems() {
        GroceryManager manager = new GroceryManager();
        GroceryItem item = new GroceryItem("Milk");
        manager.applyChanges(List.of(item));
        assertEquals(List.of(item), manager.getItems());
    }

    @Test
    void markAsDoneCommandExecutes() {
        GroceryItem item = new GroceryItem("Milk");
        Command cmd = new MarkAsDoneCommand(item);
        List<GroceryItem> result = cmd.execute(List.of(item));
        assertTrue(result.get(0).isDone());
    }

    @Test
    void commandInvokerExecutesAndUndoes() {
        CommandInvoker invoker = new CommandInvoker();
        GroceryItem item = new GroceryItem("Milk");
        Command cmd = new AddItemCommand(item);

        List<GroceryItem> items = List.of();
        items = invoker.executeCommand(cmd, items);
        assertEquals(1, items.size());

        items = invoker.undo(items);
        assertEquals(0, items.size());
    }

    @Test
    void commandInvokerRedos() {
        CommandInvoker invoker = new CommandInvoker();
        GroceryItem item = new GroceryItem("Milk");
        Command cmd = new AddItemCommand(item);

        List<GroceryItem> items = List.of();
        items = invoker.executeCommand(cmd, items);
        items = invoker.undo(items);
        items = invoker.redo(items);
        assertEquals(1, items.size());
    }

    @Test
    void groceryItemToString() {
        GroceryItem item = new GroceryItem("Milk");
        assertEquals("[ ] Milk", item.toString());

        GroceryItem doneItem = item.markAsDone();
        assertEquals("[X] Milk", doneItem.toString());
    }

    @Test
    void removeItemCommandUndo() {
        GroceryItem item = new GroceryItem("Milk");
        RemoveItemCommand cmd = new RemoveItemCommand(item);
        List<GroceryItem> items = List.of(item);

        items = cmd.execute(items);
        assertEquals(0, items.size());

        items = cmd.undo(items);
        assertEquals(1, items.size());
    }

    @Test
    void markAsDoneCommandUndo() {
        GroceryItem item = new GroceryItem("Milk");
        MarkAsDoneCommand cmd = new MarkAsDoneCommand(item);
        List<GroceryItem> items = List.of(item);

        items = cmd.execute(items);
        assertTrue(items.get(0).isDone());

        items = cmd.undo(items);
        assertFalse(items.get(0).isDone());
    }

    @Test
    void groceryItemEquality() {
        var item1 = new GroceryItem("Milk");
        var item2 = new GroceryItem("Milk");
        assertEquals(item1, item2);
        assertEquals(item1.hashCode(), item2.hashCode());
    }

    @Test
    void groceryItemInequality() {
        var item1 = new GroceryItem("Milk");
        var item2 = new GroceryItem("Bread");
        assertNotEquals(item1, item2);
    }

    @Test
    void groceryItemNullNameThrows() {
        assertThrows(NullPointerException.class, () -> new GroceryItem(null));
    }

    @Test
    void groceryItemEmptyNameThrows() {
        assertThrows(IllegalArgumentException.class, () -> new GroceryItem(""));
    }

    @Test
    void groceryItemTrimsName() {
        var item = new GroceryItem("  Milk  ");
        assertEquals("Milk", item.getName());
    }

    @Test
    void markAsDoneOnAlreadyDoneItemReturnsSame() {
        GroceryItem item = new GroceryItem("Milk").markAsDone();
        GroceryItem result = item.markAsDone();
        assertSame(item, result);
    }

    @Test
    void markAsUndoneOnAlreadyUndoneItemReturnsSame() {
        GroceryItem item = new GroceryItem("Milk");
        GroceryItem result = item.markAsUndone();
        assertSame(item, result);
    }

    @Test
    void addMultipleItems() {
        CommandInvoker invoker = new CommandInvoker();
        List<GroceryItem> items = List.of();
        items = invoker.executeCommand(new AddItemCommand(new GroceryItem("Milk")), items);
        items = invoker.executeCommand(new AddItemCommand(new GroceryItem("Bread")), items);
        items = invoker.executeCommand(new AddItemCommand(new GroceryItem("Eggs")), items);
        assertEquals(3, items.size());
    }

    @Test
    void removeNonExistentItemReturnsOriginalList() {
        GroceryItem item = new GroceryItem("Milk");
        Command cmd = new RemoveItemCommand(item);
        List<GroceryItem> items = List.of(new GroceryItem("Bread"));
        List<GroceryItem> result = cmd.execute(items);
        assertEquals(1, result.size());
        assertEquals("Bread", result.get(0).getName());
    }

    @Test
    void managerGetItemsReturnsCopy() {
        GroceryManager manager = new GroceryManager();
        manager.applyChanges(List.of(new GroceryItem("Milk")));
        List<GroceryItem> items = manager.getItems();
        items.clear();
        assertEquals(1, manager.getItems().size());
    }
}

