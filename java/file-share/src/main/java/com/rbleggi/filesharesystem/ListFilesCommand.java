package com.rbleggi.filesharesystem;

public class ListFilesCommand implements Command {
    private final FileManager fileManager;

    public ListFilesCommand(FileManager fileManager) {
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

