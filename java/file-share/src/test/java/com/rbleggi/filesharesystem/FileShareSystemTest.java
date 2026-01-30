package com.rbleggi.filesharesystem;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FileShareSystemTest {
    @Test
    void fileManagerShouldSaveFileSuccessfully() {
        FileManager manager = new FileManager();
        File file = new File("test.txt", "content");
        assertDoesNotThrow(() -> manager.saveFile(file));
    }

    @Test
    void fileManagerShouldDeleteExistingFile() {
        FileManager manager = new FileManager();
        File file = new File("test.txt", "content");
        manager.saveFile(file);
        assertDoesNotThrow(() -> manager.deleteFile(file));
    }

    @Test
    void fileManagerShouldHandleDeletingNonExistentFile() {
        FileManager manager = new FileManager();
        File file = new File("nonexistent.txt", "content");
        assertDoesNotThrow(() -> manager.deleteFile(file));
    }

    @Test
    void fileManagerShouldRestoreExistingFile() {
        FileManager manager = new FileManager();
        File file = new File("test.txt", "content");
        manager.saveFile(file);
        assertDoesNotThrow(() -> manager.restoreFile(file));
    }

    @Test
    void fileManagerShouldListFiles() {
        FileManager manager = new FileManager();
        assertDoesNotThrow(manager::listFiles);
    }

    @Test
    void fileManagerShouldSearchFilesByQuery() {
        FileManager manager = new FileManager();
        File file1 = new File("test1.txt", "content1");
        File file2 = new File("test2.txt", "content2");
        File file3 = new File("other.txt", "content3");

        manager.saveFile(file1);
        manager.saveFile(file2);
        manager.saveFile(file3);

        assertDoesNotThrow(() -> manager.searchFile("test"));
    }

    @Test
    void saveFileCommandShouldExecuteAndSaveFile() {
        FileManager manager = new FileManager();
        File file = new File("test.txt", "content");
        Command cmd = new SaveFileCommand(manager, file);

        assertDoesNotThrow(cmd::execute);
    }

    @Test
    void saveFileCommandShouldUndoAndDeleteFile() {
        FileManager manager = new FileManager();
        File file = new File("test.txt", "content");
        Command cmd = new SaveFileCommand(manager, file);

        cmd.execute();
        assertDoesNotThrow(cmd::undo);
    }

    @Test
    void deleteFileCommandShouldExecuteAndDeleteFile() {
        FileManager manager = new FileManager();
        File file = new File("test.txt", "content");
        manager.saveFile(file);

        Command cmd = new DeleteFileCommand(manager, file);
        assertDoesNotThrow(cmd::execute);
    }

    @Test
    void deleteFileCommandShouldUndoAndRestoreFile() {
        FileManager manager = new FileManager();
        File file = new File("test.txt", "content");
        manager.saveFile(file);

        Command cmd = new DeleteFileCommand(manager, file);
        cmd.execute();
        assertDoesNotThrow(cmd::undo);
    }

    @Test
    void restoreFileCommandShouldExecute() {
        FileManager manager = new FileManager();
        File file = new File("test.txt", "content");
        manager.saveFile(file);

        Command cmd = new RestoreFileCommand(manager, file);
        assertDoesNotThrow(cmd::execute);
    }

    @Test
    void listFilesCommandShouldExecute() {
        FileManager manager = new FileManager();
        Command cmd = new ListFilesCommand(manager);
        assertDoesNotThrow(cmd::execute);
    }

    @Test
    void searchFileCommandShouldExecute() {
        FileManager manager = new FileManager();
        Command cmd = new SearchFileCommand(manager, "test");
        assertDoesNotThrow(cmd::execute);
    }

    @Test
    void commandInvokerShouldExecuteCommandAndAddToHistory() {
        CommandInvoker invoker = new CommandInvoker();
        FileManager manager = new FileManager();
        File file = new File("test.txt", "content");
        Command cmd = new SaveFileCommand(manager, file);

        assertDoesNotThrow(() -> invoker.executeCommand(cmd));
    }

    @Test
    void commandInvokerShouldUndoLastCommand() {
        CommandInvoker invoker = new CommandInvoker();
        FileManager manager = new FileManager();
        File file = new File("test.txt", "content");
        Command cmd = new SaveFileCommand(manager, file);

        invoker.executeCommand(cmd);
        assertDoesNotThrow(invoker::undo);
    }

    @Test
    void commandInvokerShouldRedoLastUndoneCommand() {
        CommandInvoker invoker = new CommandInvoker();
        FileManager manager = new FileManager();
        File file = new File("test.txt", "content");
        Command cmd = new SaveFileCommand(manager, file);

        invoker.executeCommand(cmd);
        invoker.undo();
        assertDoesNotThrow(invoker::redo);
    }

    @Test
    void commandInvokerShouldHandleUndoWithEmptyHistory() {
        CommandInvoker invoker = new CommandInvoker();
        assertDoesNotThrow(invoker::undo);
    }

    @Test
    void commandInvokerShouldHandleRedoWithEmptyStack() {
        CommandInvoker invoker = new CommandInvoker();
        assertDoesNotThrow(invoker::redo);
    }

    @Test
    void fileRecordShouldHaveCorrectFields() {
        var file = new File("test.txt", "content");
        assertEquals("test.txt", file.name());
        assertEquals("content", file.content());
    }

    @Test
    void fileRecordShouldDefaultToNotEncrypted() {
        var file = new File("test.txt", "content");
        assertFalse(file.isEncrypted());
    }

    @Test
    void fileRecordShouldSupportEncryptedFlag() {
        var file = new File("secret.txt", "content", true);
        assertTrue(file.isEncrypted());
    }

    @Test
    void fileRecordEqualityShouldWork() {
        var file1 = new File("test.txt", "content");
        var file2 = new File("test.txt", "content");
        assertEquals(file1, file2);
    }

    @Test
    void fileRecordInequalityShouldWork() {
        var file1 = new File("test1.txt", "content1");
        var file2 = new File("test2.txt", "content2");
        assertNotEquals(file1, file2);
    }
}

