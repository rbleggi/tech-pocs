package com.rbleggi.filesharesystem;

public class SaveFileCommand implements Command {
    private final FileManager fileManager;
    private final File file;

    public SaveFileCommand(FileManager fileManager, File file) {
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

