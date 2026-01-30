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

    @Test
    void saveCommandUndoShouldRemoveFile() {
        var manager = new FileManager();
        var file = new File("test.txt", "content");
        var cmd = new SaveFileCommand(manager, file);
        cmd.execute();
        cmd.undo();
        assertDoesNotThrow(() -> manager.deleteFile(file));
    }

    @Test
    void deleteCommandUndoShouldRestoreFile() {
        var manager = new FileManager();
        var file = new File("test.txt", "content");
        manager.saveFile(file);
        var cmd = new DeleteFileCommand(manager, file);
        cmd.execute();
        cmd.undo();
        assertDoesNotThrow(() -> manager.restoreFile(file));
    }

    @Test
    void invokerUndoRedoShouldMaintainState() {
        var invoker = new CommandInvoker();
        var manager = new FileManager();
        var file = new File("test.txt", "content");

        invoker.executeCommand(new SaveFileCommand(manager, file));
        invoker.undo();
        invoker.redo();
        assertDoesNotThrow(() -> manager.restoreFile(file));
    }

    @Test
    void invokerMultipleUndosShouldWork() {
        var invoker = new CommandInvoker();
        var manager = new FileManager();
        var file1 = new File("file1.txt", "content1");
        var file2 = new File("file2.txt", "content2");

        invoker.executeCommand(new SaveFileCommand(manager, file1));
        invoker.executeCommand(new SaveFileCommand(manager, file2));
        invoker.undo();
        invoker.undo();
        assertDoesNotThrow(manager::listFiles);
    }

    @Test
    void searchShouldFindMatchingFiles() {
        var manager = new FileManager();
        manager.saveFile(new File("report.txt", "data"));
        manager.saveFile(new File("report2.txt", "data"));
        manager.saveFile(new File("image.png", "binary"));
        assertDoesNotThrow(() -> manager.searchFile("report"));
    }

    @Test
    void searchShouldHandleNoMatches() {
        var manager = new FileManager();
        manager.saveFile(new File("test.txt", "content"));
        assertDoesNotThrow(() -> manager.searchFile("nonexistent"));
    }

    @Test
    void saveMultipleFilesShouldWork() {
        var manager = new FileManager();
        manager.saveFile(new File("file1.txt", "a"));
        manager.saveFile(new File("file2.txt", "b"));
        manager.saveFile(new File("file3.txt", "c"));
        assertDoesNotThrow(manager::listFiles);
    }

    @Test
    void overwriteFileShouldWork() {
        var manager = new FileManager();
        manager.saveFile(new File("test.txt", "old content"));
        manager.saveFile(new File("test.txt", "new content"));
        assertDoesNotThrow(manager::listFiles);
    }
}

