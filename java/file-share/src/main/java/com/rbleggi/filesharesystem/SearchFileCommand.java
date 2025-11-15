package com.rbleggi.filesharesystem;

public class SearchFileCommand implements Command {
    private final FileManager fileManager;
    private final String query;

    public SearchFileCommand(FileManager fileManager, String query) {
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

