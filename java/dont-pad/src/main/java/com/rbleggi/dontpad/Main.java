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

class NoOpCommand implements Command {
    @Override
    public void execute() {
    }
}

public class Main {
    public static void main(String[] args) {
        String key = "/mypage";
        var notePad = new NotePad(key);

        System.out.println("[LOG] Creating NotePad for page: " + key);

        var appendCommand = new AppendNoteCommand(notePad, "Hello, world!");
        System.out.println("[LOG] Executing AppendNoteCommand: adding 'Hello, world!'");
        appendCommand.execute();

        var loadCommand = new LoadNoteCommand(notePad);
        System.out.println("[LOG] Executing LoadNoteCommand: displaying current content");
        loadCommand.execute();

        var noOpCommand = new NoOpCommand();
        System.out.println("[LOG] Executing NoOpCommand: no operation will be performed");
        noOpCommand.execute();
    }
}
