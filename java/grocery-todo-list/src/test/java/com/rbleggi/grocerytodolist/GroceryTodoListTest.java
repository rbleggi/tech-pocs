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
}

