package com.rbleggi.filesharesystem;

public class RestoreFileCommand implements Command {
    private final FileManager fileManager;
    private final File file;

    public RestoreFileCommand(FileManager fileManager, File file) {
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

