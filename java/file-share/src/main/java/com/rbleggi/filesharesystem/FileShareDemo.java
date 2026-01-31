package com.rbleggi.filesharesystem;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

record File(String name, String content, boolean isEncrypted) {
    File(String name, String content) {
        this(name, content, false);
    }
}

interface Command {
    void execute();
    void undo();
}

class FileManager {
    private final Map<String, File> files = new HashMap<>();

    void saveFile(File file) {
        files.put(file.name(), file);
        System.out.println("File '" + file.name() + "' saved.");
    }

    void restoreFile(File file) {
        if (files.containsKey(file.name())) {
            System.out.println("File '" + file.name() + "' restored.");
        } else {
            System.out.println("File '" + file.name() + "' does not exist.");
        }
    }

    void deleteFile(File file) {
        if (files.remove(file.name()) != null) {
            System.out.println("File '" + file.name() + "' deleted.");
        } else {
            System.out.println("File '" + file.name() + "' does not exist.");
        }
    }

    void listFiles() {
        if (files.isEmpty()) {
            System.out.println("\nNo files available.");
        } else {
            System.out.println("\nFiles:");
            files.keySet().forEach(System.out::println);
        }
    }

    void searchFile(String query) {
        var results = files.keySet().stream()
                .filter(name -> name.contains(query))
                .toList();
        if (results.isEmpty()) {
            System.out.println("\nNo files found matching '" + query + "'.");
        } else {
            System.out.println("\nFiles matching '" + query + "':");
            results.forEach(System.out::println);
        }
    }
}

class SaveFileCommand implements Command {
    private final FileManager fileManager;
    private final File file;

    SaveFileCommand(FileManager fileManager, File file) {
        this.fileManager = fileManager;
        this.file = file;
    }

    @Override
    public void execute() {
        fileManager.saveFile(file);
    }

    @Override
    public void undo() {
        fileManager.deleteFile(file);
    }
}

class DeleteFileCommand implements Command {
    private final FileManager fileManager;
    private final File file;

    DeleteFileCommand(FileManager fileManager, File file) {
        this.fileManager = fileManager;
        this.file = file;
    }

    @Override
    public void execute() {
        fileManager.deleteFile(file);
    }

    @Override
    public void undo() {
        fileManager.saveFile(file);
    }
}

class RestoreFileCommand implements Command {
    private final FileManager fileManager;
    private final File file;

    RestoreFileCommand(FileManager fileManager, File file) {
        this.fileManager = fileManager;
        this.file = file;
    }

    @Override
    public void execute() {
        fileManager.restoreFile(file);
    }

    @Override
    public void undo() {
        fileManager.deleteFile(file);
    }
}

class ListFilesCommand implements Command {
    private final FileManager fileManager;

    ListFilesCommand(FileManager fileManager) {
        this.fileManager = fileManager;
    }

    @Override
    public void execute() {
        fileManager.listFiles();
    }

    @Override
    public void undo() {
        System.out.println("Undo not supported for listing files.");
    }
}

class SearchFileCommand implements Command {
    private final FileManager fileManager;
    private final String query;

    SearchFileCommand(FileManager fileManager, String query) {
        this.fileManager = fileManager;
        this.query = query;
    }

    @Override
    public void execute() {
        fileManager.searchFile(query);
    }

    @Override
    public void undo() {
        System.out.println("Undo not supported for searching files.");
    }
}

class CommandInvoker {
    private final Stack<Command> history = new Stack<>();
    private final Stack<Command> redoStack = new Stack<>();

    void executeCommand(Command command) {
        command.execute();
        history.push(command);
        redoStack.clear();
    }

    void undo() {
        if (!history.isEmpty()) {
            Command command = history.pop();
            command.undo();
            redoStack.push(command);
        } else {
            System.out.println("No actions to undo.");
        }
    }

    void redo() {
        if (!redoStack.isEmpty()) {
            Command command = redoStack.pop();
            command.execute();
            history.push(command);
        } else {
            System.out.println("No actions to redo.");
        }
    }
}

public class FileShareDemo {
    public static void main(String[] args) {
        System.out.println("File Share Demo");
    }
}
