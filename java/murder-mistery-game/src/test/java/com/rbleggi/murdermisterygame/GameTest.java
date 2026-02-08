package com.rbleggi.murdermisterygame;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GameTest {
    private Game game;

    @BeforeEach
    void setUp() {
        game = new Game();
    }

    @Test
    @DisplayName("Game should initialize with suspects")
    void game_initialization_hasSuspects() {
        assertEquals(3, game.suspects().size());
        assertTrue(game.suspects().contains("Alice"));
        assertTrue(game.suspects().contains("Bob"));
        assertTrue(game.suspects().contains("Charlie"));
    }

    @Test
    @DisplayName("Game should initialize with clues")
    void game_initialization_hasClues() {
        assertEquals(3, game.clues().size());
        assertTrue(game.clues().contains("glove"));
        assertTrue(game.clues().contains("glass"));
        assertTrue(game.clues().contains("note"));
    }

    @Test
    @DisplayName("Game should not be finished initially")
    void game_initialization_notFinished() {
        assertFalse(game.finished());
    }

    @Test
    @DisplayName("Parser should parse suspects command")
    void parser_suspectsCommand_returnsCorrectType() {
        var command = Parser.parse("suspects");
        assertInstanceOf(SuspectsCommand.class, command);
    }

    @Test
    @DisplayName("Parser should parse clues command")
    void parser_cluesCommand_returnsCorrectType() {
        var command = Parser.parse("clues");
        assertInstanceOf(CluesCommand.class, command);
    }

    @Test
    @DisplayName("Parser should parse find command")
    void parser_findCommand_returnsCorrectType() {
        var command = Parser.parse("find glove");
        assertInstanceOf(FindClueCommand.class, command);
    }

    @Test
    @DisplayName("Parser should parse accuse command")
    void parser_accuseCommand_returnsCorrectType() {
        var command = Parser.parse("accuse Bob");
        assertInstanceOf(AccuseCommand.class, command);
    }

    @Test
    @DisplayName("Parser should parse help command")
    void parser_helpCommand_returnsCorrectType() {
        var command = Parser.parse("help");
        assertInstanceOf(HelpCommand.class, command);
    }

    @Test
    @DisplayName("Parser should parse exit command")
    void parser_exitCommand_returnsCorrectType() {
        var command = Parser.parse("exit");
        assertInstanceOf(ExitCommand.class, command);
    }

    @Test
    @DisplayName("Parser should handle unknown command")
    void parser_unknownCommand_returnsUnknownType() {
        var command = Parser.parse("invalid");
        assertInstanceOf(UnknownCommand.class, command);
    }

    @Test
    @DisplayName("SuspectsCommand should not change game state")
    void suspectsCommand_execute_doesNotChangeState() {
        var command = new SuspectsCommand();
        var newGame = command.execute(game);
        assertEquals(game, newGame);
    }

    @Test
    @DisplayName("FindClueCommand should add found clue to game")
    void findClueCommand_validClue_addsToFoundClues() {
        var command = new FindClueCommand("glove");
        var newGame = command.execute(game);
        assertTrue(newGame.foundClues().contains("glove"));
    }

    @Test
    @DisplayName("FindClueCommand should not add invalid clue")
    void findClueCommand_invalidClue_doesNotAddToFoundClues() {
        var command = new FindClueCommand("invalid");
        var newGame = command.execute(game);
        assertFalse(newGame.foundClues().contains("invalid"));
    }

    @Test
    @DisplayName("FindClueCommand should not add duplicate clue")
    void findClueCommand_duplicateClue_doesNotAddAgain() {
        var game1 = game.withClue("glove");
        var command = new FindClueCommand("glove");
        var game2 = command.execute(game1);
        assertEquals(1, game2.foundClues().size());
    }

    @Test
    @DisplayName("AccuseCommand with Bob should win game")
    void accuseCommand_bob_winsGame() {
        var command = new AccuseCommand("Bob");
        var newGame = command.execute(game);
        assertTrue(newGame.finished());
        assertEquals("Bob", newGame.accused());
    }

    @Test
    @DisplayName("AccuseCommand with wrong suspect should end game")
    void accuseCommand_wrongSuspect_endsGame() {
        var command = new AccuseCommand("Alice");
        var newGame = command.execute(game);
        assertTrue(newGame.finished());
        assertEquals("Alice", newGame.accused());
    }

    @Test
    @DisplayName("AccuseCommand with invalid suspect should not change game")
    void accuseCommand_invalidSuspect_doesNotChangeState() {
        var command = new AccuseCommand("Unknown");
        var newGame = command.execute(game);
        assertFalse(newGame.finished());
    }

    @Test
    @DisplayName("ExitCommand should finish game")
    void exitCommand_execute_finishesGame() {
        var command = new ExitCommand();
        var newGame = command.execute(game);
        assertTrue(newGame.finished());
    }

    @Test
    @DisplayName("HelpCommand should not change game state")
    void helpCommand_execute_doesNotChangeState() {
        var command = new HelpCommand();
        var newGame = command.execute(game);
        assertEquals(game, newGame);
    }
}
