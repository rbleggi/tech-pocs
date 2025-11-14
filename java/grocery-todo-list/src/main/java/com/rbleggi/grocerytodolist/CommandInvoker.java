package com.rbleggi.grocerytodolist;

import java.util.List;
import java.util.Stack;

public class CommandInvoker {
    private final Stack<Command> history = new Stack<>();
    private final Stack<Command> redoStack = new Stack<>();

    public List<GroceryItem> executeCommand(Command command, List<GroceryItem> items) {
        List<GroceryItem> result = command.execute(items);
        history.push(command);
        redoStack.clear();
        return result;
    }

    public List<GroceryItem> undo(List<GroceryItem> items) {
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

    public List<GroceryItem> redo(List<GroceryItem> items) {
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

