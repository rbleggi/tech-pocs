package com.rbleggi.filesharesystem;

import java.util.Stack;

public class CommandInvoker {
    private final Stack<Command> history = new Stack<>();
    private final Stack<Command> redoStack = new Stack<>();

    public void executeCommand(Command command) {
        command.execute();
        history.push(command);
        redoStack.clear();
    }

    public void undo() {
        if (!history.isEmpty()) {
            Command command = history.pop();
            command.undo();
            redoStack.push(command);
        } else {
            System.out.println("No actions to undo.");
        }
    }

    public void redo() {
        if (!redoStack.isEmpty()) {
            Command command = redoStack.pop();
            command.execute();
            history.push(command);
        } else {
            System.out.println("No actions to redo.");
        }
    }
}

