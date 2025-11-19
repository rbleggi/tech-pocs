package com.rbleggi.notetaking;

import java.io.IOException;

public class SaveNotesCommand implements Command {
    private final NoteManager manager;
    private final String filePath;

    public SaveNotesCommand(NoteManager manager, String filePath) {
        this.manager = manager;
        this.filePath = filePath;
    }

    @Override
    public void execute() throws IOException {
        manager.saveNotes(filePath);
    }
}
