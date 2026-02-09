package com.rbleggi.notetaking;

import org.junit.jupiter.api.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class NoteManagerTest {
    private NoteManager manager;
    private static final String TEST_FILE = "test_notes.txt";

    @BeforeEach
    void setUp() {
        manager = new NoteManager();
    }

    @AfterEach
    void tearDown() throws IOException {
        Files.deleteIfExists(Path.of(TEST_FILE));
    }

    @Test
    @DisplayName("AddNoteCommand should add note to manager")
    void addNoteCommand_addsNote() throws Exception {
        var command = new AddNoteCommand(manager, "Title", "Content");
        command.execute();

        var notes = manager.listNotes();
        assertEquals(1, notes.size());
        assertEquals("Title", notes.get(0).title());
        assertEquals("Content", notes.get(0).content());
    }

    @Test
    @DisplayName("NoteManager should add multiple notes with unique IDs")
    void noteManager_addMultipleNotes_assignsUniqueIds() {
        var note1 = manager.addNote("First", "Content 1");
        var note2 = manager.addNote("Second", "Content 2");

        assertNotEquals(note1.id(), note2.id());
        assertEquals(2, manager.listNotes().size());
    }

    @Test
    @DisplayName("NoteManager should edit existing note")
    void noteManager_editNote_updatesContent() {
        var note = manager.addNote("Original", "Original Content");
        var edited = manager.editNote(note.id(), Optional.of("Updated"), Optional.empty());

        assertTrue(edited.isPresent());
        assertEquals("Updated", edited.get().title());
        assertEquals("Original Content", edited.get().content());
    }

    @Test
    @DisplayName("NoteManager should delete note")
    void noteManager_deleteNote_removesNote() {
        var note = manager.addNote("To Delete", "Content");
        assertTrue(manager.deleteNote(note.id()));
        assertEquals(0, manager.listNotes().size());
    }

    @Test
    @DisplayName("NoteManager should return false when deleting non-existent note")
    void noteManager_deleteNonExistent_returnsFalse() {
        assertFalse(manager.deleteNote(999));
    }

    @Test
    @DisplayName("SaveNotesCommand should save notes to file")
    void saveNotesCommand_savesToFile() throws Exception {
        manager.addNote("Note 1", "Content 1");
        manager.addNote("Note 2", "Content 2");

        var command = new SaveNotesCommand(manager, TEST_FILE);
        command.execute();

        assertTrue(Files.exists(Path.of(TEST_FILE)));
    }

    @Test
    @DisplayName("LoadNotesCommand should load notes from file")
    void loadNotesCommand_loadsFromFile() throws Exception {
        manager.addNote("Note 1", "Content 1");
        manager.addNote("Note 2", "Content 2");
        manager.saveNotes(TEST_FILE);

        var newManager = new NoteManager();
        var command = new LoadNotesCommand(newManager, TEST_FILE);
        command.execute();

        assertEquals(2, newManager.listNotes().size());
    }

    @Test
    @DisplayName("NoteManager should preserve note IDs after save and load")
    void noteManager_saveAndLoad_preservesIds() throws Exception {
        var note1 = manager.addNote("Note 1", "Content 1");
        var note2 = manager.addNote("Note 2", "Content 2");
        manager.saveNotes(TEST_FILE);

        var newManager = new NoteManager();
        newManager.loadNotes(TEST_FILE);
        var loadedNotes = newManager.listNotes();

        assertTrue(loadedNotes.stream().anyMatch(n -> n.id() == note1.id()));
        assertTrue(loadedNotes.stream().anyMatch(n -> n.id() == note2.id()));
    }

    @Test
    @DisplayName("LoadNotesCommand should handle non-existent file")
    void loadNotesCommand_nonExistentFile_doesNotThrow() throws Exception {
        var command = new LoadNotesCommand(manager, "non_existent.txt");
        assertDoesNotThrow(() -> command.execute());
    }
}
