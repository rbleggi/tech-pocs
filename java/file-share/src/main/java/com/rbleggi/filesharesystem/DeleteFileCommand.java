package com.rbleggi.filesharesystem;

public class DeleteFileCommand implements Command {
    private final FileManager fileManager;
    private final File file;

    public DeleteFileCommand(FileManager fileManager, File file) {
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

