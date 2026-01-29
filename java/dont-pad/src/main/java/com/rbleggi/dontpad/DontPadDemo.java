package com.rbleggi.dontpad;

import java.util.ArrayList;
import java.util.List;

class NotePad {
    private final String key;
    private final List<String> notes = new ArrayList<>();

    public NotePad(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }

    public String getAllText() {
        return String.join("\n", notes);
    }

    public void setAllText(String text) {
        notes.clear();
        notes.addAll(List.of(text.split("\n")));
    }

    public void appendText(String text) {
        notes.add(text);
    }
}

interface Command {
    void execute();
}

class LoadNoteCommand implements Command {
    private final NotePad notePad;

    public LoadNoteCommand(NotePad notePad) {
        this.notePad = notePad;
    }

    @Override
    public void execute() {
        System.out.println(notePad.getAllText());
    }
}

class AppendNoteCommand implements Command {
    private final NotePad notePad;
    private final String text;

    public AppendNoteCommand(NotePad notePad, String text) {
        this.notePad = notePad;
        this.text = text;
    }

    @Override
    public void execute() {
        notePad.appendText(text);
    }
}

class SetAllTextCommand implements Command {
    private final NotePad notePad;
    private final String text;

    public SetAllTextCommand(NotePad notePad, String text) {
        this.notePad = notePad;
        this.text = text;
    }

    @Override
    public void execute() {
        notePad.setAllText(text);
    }
}

class ClearNoteCommand implements Command {
    private final NotePad notePad;

    public ClearNoteCommand(NotePad notePad) {
        this.notePad = notePad;
    }

    @Override
    public void execute() {
        notePad.setAllText("");
    }
}

class NoOpCommand implements Command {
    @Override
    public void execute() {
    }
}

public class DontPadDemo {
    public static void main(String[] args) {
        System.out.println("DontPad Demo");
    }
}
