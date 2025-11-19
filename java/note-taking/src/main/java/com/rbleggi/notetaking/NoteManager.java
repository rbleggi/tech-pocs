package com.rbleggi.notetaking;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class NoteManager {
    private final Map<Integer, Note> notes = new HashMap<>();
    private int nextId = 1;

    public Note addNote(String title, String content) {
        var note = new Note(nextId, title, content);
        notes.put(nextId, note);
        nextId++;
        return note;
    }

    public Optional<Note> editNote(int id, Optional<String> newTitle, Optional<String> newContent) {
        return Optional.ofNullable(notes.get(id)).map(note -> {
            var updatedNote = new Note(
                note.id(),
                newTitle.orElse(note.title()),
                newContent.orElse(note.content())
            );
            notes.put(id, updatedNote);
            return updatedNote;
        });
    }

    public boolean deleteNote(int id) {
        return notes.remove(id) != null;
    }

    public List<Note> listNotes() {
        return new ArrayList<>(notes.values());
    }

    public void saveNotes(String filePath) throws IOException {
        try (var bw = new BufferedWriter(new FileWriter(filePath))) {
            for (var note : notes.values()) {
                bw.write(note.id() + "|" + note.title() + "|" + note.content() + "\n");
            }
        }
    }

    public void loadNotes(String filePath) throws IOException {
        if (Files.exists(Path.of(filePath))) {
            try (var br = new BufferedReader(new FileReader(filePath))) {
                String line;
                while ((line = br.readLine()) != null) {
                    String[] parts = line.split("\\|", 3);
                    int id = Integer.parseInt(parts[0]);
                    notes.put(id, new Note(id, parts[1], parts[2]));
                    nextId = Math.max(nextId, id + 1);
                }
            }
        }
    }
}
