package com.rbleggi.notetaking

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import java.io.File

class CommandTest {

    private lateinit var manager: NoteManager
    private lateinit var commandManager: CommandManager

    @BeforeEach
    fun setup() {
        manager = NoteManager()
        commandManager = CommandManager()
    }

    @Test
    fun `add note command should add note`() {
        commandManager.executeCommand(AddNoteCommand(manager, "Test", "Content"))
        assertEquals(1, manager.listNotes().size)
    }

    @Test
    fun `edit note command should edit note`() {
        manager.addNote("Original", "Original Content")
        commandManager.executeCommand(EditNoteCommand(manager, 1, "Updated", "Updated Content"))
        val note = manager.listNotes().first()
        assertEquals("Updated", note.title)
        assertEquals("Updated Content", note.content)
    }

    @Test
    fun `delete note command should delete note`() {
        manager.addNote("Test", "Content")
        commandManager.executeCommand(DeleteNoteCommand(manager, 1))
        assertEquals(0, manager.listNotes().size)
    }

    @Test
    fun `save notes command should save notes to file`() {
        val testFile = "test-save-notes.txt"
        manager.addNote("Note 1", "Content 1")
        commandManager.executeCommand(SaveNotesCommand(manager, testFile))
        assertTrue(File(testFile).exists())
        File(testFile).delete()
    }

    @Test
    fun `load notes command should load notes from file`() {
        val testFile = "test-load-notes.txt"
        manager.addNote("Note 1", "Content 1")
        manager.saveNotes(testFile)

        val newManager = NoteManager()
        commandManager.executeCommand(LoadNotesCommand(newManager, testFile))
        assertEquals(1, newManager.listNotes().size)
        File(testFile).delete()
    }
}
