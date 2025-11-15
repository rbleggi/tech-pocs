package com.rbleggi.filesharesystem;

public class Main {
    public static void main(String[] args) {
        FileManager fileManager = new FileManager();
        CommandInvoker invoker = new CommandInvoker();

        File file1 = new File("example1.txt", "This is the content of file 1.");
        File file2 = new File("example2.txt", "This is the content of file 2.");
        File file3 = new File("example3.txt", "This is the content of file 3.");

        invoker.executeCommand(new SaveFileCommand(fileManager, file1));
        invoker.executeCommand(new SaveFileCommand(fileManager, file2));
        invoker.executeCommand(new ListFilesCommand(fileManager));
        invoker.executeCommand(new SearchFileCommand(fileManager, "example1"));
        invoker.executeCommand(new DeleteFileCommand(fileManager, file1));
        invoker.undo();
        invoker.redo();
        invoker.executeCommand(new RestoreFileCommand(fileManager, file3));
        invoker.executeCommand(new ListFilesCommand(fileManager));
        invoker.undo();
        invoker.executeCommand(new ListFilesCommand(fileManager));
    }
}

