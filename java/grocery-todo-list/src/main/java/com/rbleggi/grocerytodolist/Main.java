package com.rbleggi.grocerytodolist;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Stack;

final class GroceryItem {
    private final String name;
    private final boolean done;

    GroceryItem(String name) { this(name, false); }

    GroceryItem(String name, boolean done) {
        String n = Objects.requireNonNull(name, "name").trim();
        if (n.isEmpty()) throw new IllegalArgumentException("name cannot be empty");
        this.name = n;
        this.done = done;
    }

    String getName() { return name; }
    boolean isDone() { return done; }

    GroceryItem markAsDone() { return done ? this : new GroceryItem(name, true); }
    GroceryItem markAsUndone() { return !done ? this : new GroceryItem(name, false); }

    @Override public String toString() { return (done ? "[X] " : "[ ] ") + name; }

    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GroceryItem that = (GroceryItem) o;
        return name.equals(that.name);
    }

    @Override public int hashCode() { return name.hashCode(); }
}

interface Command {
    List<GroceryItem> execute(List<GroceryItem> items);
    List<GroceryItem> undo(List<GroceryItem> items);
}

class GroceryManager {
    private List<GroceryItem> items = new ArrayList<>();

    List<GroceryItem> getItems() {
        return new ArrayList<>(items);
    }

    void applyChanges(List<GroceryItem> newItems) {
        items = new ArrayList<>(newItems);
    }

    void listAll() {
        System.out.println("\nGrocery List:");
        if (items.isEmpty()) {
            System.out.println("No items in the list.");
        } else {
            items.forEach(System.out::println);
        }
    }
}

class AddItemCommand implements Command {
    private final GroceryItem item;

    AddItemCommand(GroceryItem item) {
        this.item = item;
    }

    @Override
    public List<GroceryItem> execute(List<GroceryItem> items) {
        if (items.stream().noneMatch(i -> i.getName().equals(item.getName()))) {
            System.out.println("Added: " + item.getName());
            List<GroceryItem> newItems = new ArrayList<>(items);
            newItems.add(item);
            return newItems;
        } else {
            System.out.println("Item already exists: " + item.getName());
            return items;
        }
    }

    @Override
    public List<GroceryItem> undo(List<GroceryItem> items) {
        int index = -1;
        for (int i = 0; i < items.size(); i++) {
            if (items.get(i).getName().equals(item.getName())) {
                index = i;
                break;
            }
        }
        if (index >= 0) {
            System.out.println("Undone addition: " + item.getName());
            List<GroceryItem> newItems = new ArrayList<>(items);
            newItems.remove(index);
            return newItems;
        } else {
            System.out.println("Item not found for undo: " + item.getName());
            return items;
        }
    }
}

class RemoveItemCommand implements Command {
    private final GroceryItem item;
    private GroceryItem removedItem;

    RemoveItemCommand(GroceryItem item) {
        this.item = item;
    }

    @Override
    public List<GroceryItem> execute(List<GroceryItem> items) {
        int index = -1;
        for (int i = 0; i < items.size(); i++) {
            if (items.get(i).getName().equals(item.getName())) {
                index = i;
                break;
            }
        }
        if (index >= 0) {
            removedItem = items.get(index);
            System.out.println("Removed: " + item.getName());
            List<GroceryItem> newItems = new ArrayList<>(items);
            newItems.remove(index);
            return newItems;
        } else {
            System.out.println("Item not found: " + item.getName());
            return items;
        }
    }

    @Override
    public List<GroceryItem> undo(List<GroceryItem> items) {
        if (removedItem != null) {
            System.out.println("Undone removal: " + removedItem.getName());
            List<GroceryItem> newItems = new ArrayList<>(items);
            newItems.add(removedItem);
            return newItems;
        } else {
            System.out.println("Cannot undo removal: " + item.getName());
            return items;
        }
    }
}

class MarkAsDoneCommand implements Command {
    private final GroceryItem item;
    private GroceryItem previousState;

    MarkAsDoneCommand(GroceryItem item) {
        this.item = item;
    }

    @Override
    public List<GroceryItem> execute(List<GroceryItem> items) {
        int index = -1;
        for (int i = 0; i < items.size(); i++) {
            if (items.get(i).getName().equals(item.getName())) {
                index = i;
                break;
            }
        }
        if (index >= 0) {
            previousState = items.get(index);
            GroceryItem updatedItem = items.get(index).markAsDone();
            System.out.println("Marked as done: " + item.getName());
            List<GroceryItem> newItems = new ArrayList<>(items);
            newItems.set(index, updatedItem);
            return newItems;
        } else {
            System.out.println("Item not found: " + item.getName());
            return items;
        }
    }

    @Override
    public List<GroceryItem> undo(List<GroceryItem> items) {
        if (previousState != null) {
            int index = -1;
            for (int i = 0; i < items.size(); i++) {
                if (items.get(i).getName().equals(item.getName())) {
                    index = i;
                    break;
                }
            }
            if (index >= 0) {
                System.out.println("Undone mark as done: " + item.getName());
                List<GroceryItem> newItems = new ArrayList<>(items);
                newItems.set(index, previousState);
                return newItems;
            } else {
                System.out.println("Item not found for undo: " + item.getName());
                return items;
            }
        } else {
            System.out.println("Cannot undo: " + item.getName());
            return items;
        }
    }
}

class CommandInvoker {
    private final Stack<Command> history = new Stack<>();
    private final Stack<Command> redoStack = new Stack<>();

    List<GroceryItem> executeCommand(Command command, List<GroceryItem> items) {
        List<GroceryItem> result = command.execute(items);
        history.push(command);
        redoStack.clear();
        return result;
    }

    List<GroceryItem> undo(List<GroceryItem> items) {
        if (!history.isEmpty()) {
            Command command = history.pop();
            List<GroceryItem> result = command.undo(items);
            redoStack.push(command);
            System.out.println("Undo successful");
            return result;
        } else {
            System.out.println("Nothing to undo");
            return items;
        }
    }

    List<GroceryItem> redo(List<GroceryItem> items) {
        if (!redoStack.isEmpty()) {
            Command command = redoStack.pop();
            List<GroceryItem> result = command.execute(items);
            history.push(command);
            System.out.println("Redo successful");
            return result;
        } else {
            System.out.println("Nothing to redo");
            return items;
        }
    }
}

public class Main {
    public static void main(String[] args) {
        System.out.println("Grocery Todo List");
    }
}
