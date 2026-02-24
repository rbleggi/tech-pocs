package com.rbleggi.murdermisterygame;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        System.out.println("Murder Mistery Game");
    }
}

interface Command {
    Game execute(Game game);
}

record Game(
    List<String> suspects,
    List<String> clues,
    List<String> foundClues,
    String accused,
    boolean finished
) {
    Game() {
        this(List.of("Alice", "Bob", "Charlie"), List.of("glove", "glass", "note"), List.of(), null, false);
    }

    Game withClue(String clue) {
        var newFoundClues = new ArrayList<>(foundClues);
        newFoundClues.add(clue);
        return new Game(suspects, clues, newFoundClues, accused, finished);
    }

    Game withAccused(String suspect) {
        return new Game(suspects, clues, foundClues, suspect, true);
    }

    Game finish() {
        return new Game(suspects, clues, foundClues, accused, true);
    }
}

class Parser {
    static Command parse(String input) {
        if (input.equalsIgnoreCase("suspects")) return new SuspectsCommand();
        if (input.equalsIgnoreCase("clues")) return new CluesCommand();
        if (input.toLowerCase().startsWith("find ")) return new FindClueCommand(input.substring(5).trim());
        if (input.toLowerCase().startsWith("accuse ")) return new AccuseCommand(input.substring(7).trim());
        if (input.equalsIgnoreCase("help")) return new HelpCommand();
        if (input.equalsIgnoreCase("exit")) return new ExitCommand();
        return new UnknownCommand();
    }
}

class SuspectsCommand implements Command {
    @Override
    public Game execute(Game game) {
        System.out.println("Suspects: " + String.join(", ", game.suspects()));
        return game;
    }
}

class CluesCommand implements Command {
    @Override
    public Game execute(Game game) {
        var foundCluesStr = game.foundClues().isEmpty() ? "none" : String.join(", ", game.foundClues());
        System.out.println("Found clues: " + foundCluesStr);
        var availableClues = game.clues().stream()
            .filter(c -> !game.foundClues().contains(c))
            .toList();
        System.out.println("Available clues: " + String.join(", ", availableClues));
        return game;
    }
}

class FindClueCommand implements Command {
    private final String clue;

    FindClueCommand(String clue) {
        this.clue = clue;
    }

    @Override
    public Game execute(Game game) {
        if (game.clues().contains(clue) && !game.foundClues().contains(clue)) {
            System.out.println("You found the clue: " + clue + "!");
            return game.withClue(clue);
        } else if (game.foundClues().contains(clue)) {
            System.out.println("You have already found this clue.");
            return game;
        } else {
            System.out.println("Clue not found.");
            return game;
        }
    }
}

class AccuseCommand implements Command {
    private final String suspect;

    AccuseCommand(String suspect) {
        this.suspect = suspect;
    }

    @Override
    public Game execute(Game game) {
        if (game.suspects().contains(suspect)) {
            var correct = suspect.equals("Bob");
            System.out.println("You accused " + suspect + ".");
            if (correct) System.out.println("Congratulations! You solved the mystery!");
            else System.out.println("Wrong! The real culprit was Bob.");
            return game.withAccused(suspect);
        } else {
            System.out.println("Suspect not found.");
            return game;
        }
    }
}

class HelpCommand implements Command {
    @Override
    public Game execute(Game game) {
        System.out.println("Available commands:");
        System.out.println("suspects - list the suspects");
        System.out.println("clues - show found and available clues");
        System.out.println("find <clue> - find a clue");
        System.out.println("accuse <suspect> - accuse a suspect");
        System.out.println("help - show this menu");
        System.out.println("exit - end the game");
        return game;
    }
}

class ExitCommand implements Command {
    @Override
    public Game execute(Game game) {
        System.out.println("Game ended.");
        return game.finish();
    }
}

class UnknownCommand implements Command {
    @Override
    public Game execute(Game game) {
        System.out.println("Unknown command. Type 'help' to see the commands.");
        return game;
    }
}
