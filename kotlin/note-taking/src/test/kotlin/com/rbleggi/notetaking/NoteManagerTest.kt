package com.rbleggi.notetaking

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import java.io.File

class NoteManagerTest {

    private lateinit var manager: NoteManager

    @BeforeEach
    fun setup() {
        manager = NoteManager()
    }

    @Test
    fun `add note should create a new note`() {
        val note = manager.addNote("Test Note", "Test Content")
        assertEquals(1, note.id)
        assertEquals("Test Note", note.title)
        assertEquals("Test Content", note.content)
    }

    @Test
    fun `add multiple notes should increment id`() {
        val note1 = manager.addNote("Note 1", "Content 1")
        val note2 = manager.addNote("Note 2", "Content 2")
        assertEquals(1, note1.id)
        assertEquals(2, note2.id)
    }

    @Test
    fun `edit note should update title and content`() {
        manager.addNote("Original", "Original Content")
        val updated = manager.editNote(1, "Updated", "Updated Content")
        assertNotNull(updated)
        assertEquals("Updated", updated?.title)
        assertEquals("Updated Content", updated?.content)
    }

    @Test
    fun `edit note with only title should keep original content`() {
        manager.addNote("Original", "Original Content")
        val updated = manager.editNote(1, newTitle = "Updated")
        assertNotNull(updated)
        assertEquals("Updated", updated?.title)
        assertEquals("Original Content", updated?.content)
    }

    @Test
    fun `edit note with only content should keep original title`() {
        manager.addNote("Original", "Original Content")
        val updated = manager.editNote(1, newContent = "Updated Content")
        assertNotNull(updated)
        assertEquals("Original", updated?.title)
        assertEquals("Updated Content", updated?.content)
    }

    @Test
    fun `edit non-existent note should return null`() {
        val updated = manager.editNote(999, "Updated", "Updated Content")
        assertNull(updated)
    }

    @Test
    fun `delete note should remove it from manager`() {
        manager.addNote("Test Note", "Test Content")
        val deleted = manager.deleteNote(1)
        assertTrue(deleted)
        assertEquals(0, manager.listNotes().size)
    }

    @Test
    fun `delete non-existent note should return false`() {
        val deleted = manager.deleteNote(999)
        assertFalse(deleted)
    }

    @Test
    fun `list notes should return all notes`() {
        manager.addNote("Note 1", "Content 1")
        manager.addNote("Note 2", "Content 2")
        manager.addNote("Note 3", "Content 3")
        val notes = manager.listNotes()
        assertEquals(3, notes.size)
    }

    @Test
    fun `save and load notes should persist data`() {
        val testFile = "test-notes.txt"
        manager.addNote("Note 1", "Content 1")
        manager.addNote("Note 2", "Content 2")
        manager.saveNotes(testFile)

        val newManager = NoteManager()
        newManager.loadNotes(testFile)
        val notes = newManager.listNotes()
        assertEquals(2, notes.size)
        assertEquals("Note 1", notes.find { it.id == 1 }?.title)
        assertEquals("Note 2", notes.find { it.id == 2 }?.title)

        File(testFile).delete()
    }
}
