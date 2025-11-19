package com.rbleggi.notetaking;

public class AddNoteCommand implements Command {
    private final NoteManager manager;
    private final String title;
    private final String content;

    public AddNoteCommand(NoteManager manager, String title, String content) {
        this.manager = manager;
        this.title = title;
        this.content = content;
    }

    @Override
    public void execute() {
        manager.addNote(title, content);
    }
}
