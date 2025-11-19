package com.rbleggi.notetaking;

public class Main {
    public static void main(String[] args) throws Exception {
        var manager = new NoteManager();

        new AddNoteCommand(manager, "First Note", "This is the content of the first note.").execute();
        new AddNoteCommand(manager, "Second Note", "This is the content of the second note.").execute();
        new SaveNotesCommand(manager, "notes.txt").execute();
        System.out.println("Notes saved to file.");

        new LoadNotesCommand(manager, "notes.txt").execute();
        System.out.println("Notes loaded from file:");
        manager.listNotes().forEach(System.out::println);
    }
}
