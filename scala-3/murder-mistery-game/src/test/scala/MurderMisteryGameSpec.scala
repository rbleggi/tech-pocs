package com.rbleggi.murdermistery

import com.rbleggi.murdermisterygame._

class MurderMisteryGameSpec {

  test("Game initializes with suspects and clues") {
    val suspects = List("Ms. Scarlet", "Col. Mustard", "Prof. Plum")
    val clues = List("red scarf", "mustard stain")
    val game = Game(suspects, clues, Nil, None, finished = false)

    assert(game.suspects == suspects)
    assert(game.clues == clues)
    assert(game.foundClues.isEmpty)
    assert(game.accused.isEmpty)
    assert(!game.finished)
  }

  test("FindClue command adds clue to foundClues") {
    val game = Game(List("Ms. Scarlet"), List("red scarf", "knife"), Nil, None, finished = false)
    val updatedGame = Commands.FindClue("red scarf").execute(game)

    assert(updatedGame.foundClues.contains("red scarf"))
  }

  test("FindClue command does not add duplicate clues") {
    val game = Game(List("Ms. Scarlet"), List("red scarf"), List("red scarf"), None, finished = false)
    val updatedGame = Commands.FindClue("red scarf").execute(game)

    assert(updatedGame.foundClues.count(_ == "red scarf") == 1)
  }

  test("FindClue command does not add non-existent clues") {
    val game = Game(List("Ms. Scarlet"), List("red scarf"), Nil, None, finished = false)
    val updatedGame = Commands.FindClue("blue hat").execute(game)

    assert(updatedGame.foundClues.isEmpty)
  }

  test("Accuse command with correct suspect solves the game") {
    val game = Game(List("Ms. Scarlet", "Col. Mustard"), List("red scarf"), Nil, None, finished = false)
    val updatedGame = Commands.Accuse("Ms. Scarlet").execute(game)

    assert(updatedGame.accused.contains("Ms. Scarlet"))
    assert(updatedGame.finished)
  }

  test("Accuse command with wrong suspect ends the game") {
    val game = Game(List("Ms. Scarlet", "Col. Mustard"), List("red scarf"), Nil, None, finished = false)
    val updatedGame = Commands.Accuse("Col. Mustard").execute(game)

    assert(updatedGame.accused.contains("Col. Mustard"))
    assert(updatedGame.finished)
  }

  test("Accuse command with non-existent suspect does not end the game") {
    val game = Game(List("Ms. Scarlet"), List("red scarf"), Nil, None, finished = false)
    val updatedGame = Commands.Accuse("Dr. Evil").execute(game)

    assert(updatedGame.accused.isEmpty)
    assert(!updatedGame.finished)
  }

  test("ShowSuspects command does not change game state") {
    val game = Game(List("Ms. Scarlet", "Col. Mustard"), List("red scarf"), Nil, None, finished = false)
    val updatedGame = Commands.ShowSuspects.execute(game)

    assert(updatedGame == game)
  }

  test("ShowClues command does not change game state") {
    val game = Game(List("Ms. Scarlet"), List("red scarf", "knife"), Nil, None, finished = false)
    val updatedGame = Commands.ShowClues.execute(game)

    assert(updatedGame == game)
  }

  test("Exit command ends the game") {
    val game = Game(List("Ms. Scarlet"), List("red scarf"), Nil, None, finished = false)
    val updatedGame = Commands.Exit.execute(game)

    assert(updatedGame.finished)
  }

  test("Parser parses 'suspects' command") {
    val cmd = Parser.parse("suspects")
    assert(cmd == Commands.ShowSuspects)
  }

  test("Parser parses 'clues' command") {
    val cmd = Parser.parse("clues")
    assert(cmd == Commands.ShowClues)
  }

  test("Parser parses 'find <clue>' command") {
    val cmd = Parser.parse("find red scarf")
    assert(cmd == Commands.FindClue("red scarf"))
  }

  test("Parser parses 'accuse <suspect>' command") {
    val cmd = Parser.parse("accuse Ms. Scarlet")
    assert(cmd == Commands.Accuse("Ms. Scarlet"))
  }

  test("Parser parses 'help' command") {
    val cmd = Parser.parse("help")
    assert(cmd == Commands.Help)
  }

  test("Parser parses 'exit' command") {
    val cmd = Parser.parse("exit")
    assert(cmd == Commands.Exit)
  }

  test("Parser returns Help for unknown commands") {
    val cmd = Parser.parse("unknown")
    assert(cmd == Commands.Help)
  }
}
