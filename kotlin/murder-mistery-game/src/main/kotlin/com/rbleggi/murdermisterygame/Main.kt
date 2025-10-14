package com.rbleggi.murdermisterygame

fun main() {
    val initialGame = Game()
    println("Welcome to the Murder Mystery Game!")
    println("Type 'help' to see the available commands.")
    var game = initialGame
    while (!game.finished) {
        print("> ")
        val input = readLine() ?: ""
        val command = Parser.parse(input)
        game = command.execute(game)
    }
}

interface Command {
    fun execute(game: Game): Game
}

class Game(
    val suspects: List<String> = listOf("Alice", "Bob", "Charlie"),
    val clues: List<String> = listOf("glove", "glass", "note"),
    val foundClues: List<String> = emptyList(),
    val accused: String? = null,
    val finished: Boolean = false
) {
    fun withClue(clue: String) = copy(foundClues = foundClues + clue)
    fun withAccused(suspect: String) = copy(accused = suspect, finished = true)
    fun finish() = copy(finished = true)
    private fun copy(
        suspects: List<String> = this.suspects,
        clues: List<String> = this.clues,
        foundClues: List<String> = this.foundClues,
        accused: String? = this.accused,
        finished: Boolean = this.finished
    ) = Game(suspects, clues, foundClues, accused, finished)
}

object Parser {
    fun parse(input: String): Command = when {
        input.equals("suspects", true) -> SuspectsCommand
        input.equals("clues", true) -> CluesCommand
        input.startsWith("find ", true) -> FindClueCommand(input.drop(5).trim())
        input.startsWith("accuse ", true) -> AccuseCommand(input.drop(7).trim())
        input.equals("help", true) -> HelpCommand
        input.equals("exit", true) -> ExitCommand
        else -> UnknownCommand
    }
}

object SuspectsCommand : Command {
    override fun execute(game: Game): Game {
        println("Suspects: ${game.suspects.joinToString(", ")}")
        return game
    }
}

object CluesCommand : Command {
    override fun execute(game: Game): Game {
        println("Found clues: ${if (game.foundClues.isEmpty()) "none" else game.foundClues.joinToString(", ")}")
        println("Available clues: ${game.clues.filter { it !in game.foundClues }.joinToString(", ")}")
        return game
    }
}

class FindClueCommand(private val clue: String) : Command {
    override fun execute(game: Game): Game {
        return if (clue in game.clues && clue !in game.foundClues) {
            println("You found the clue: $clue!")
            game.withClue(clue)
        } else if (clue in game.foundClues) {
            println("You have already found this clue.")
            game
        } else {
            println("Clue not found.")
            game
        }
    }
}

class AccuseCommand(private val suspect: String) : Command {
    override fun execute(game: Game): Game {
        return if (suspect in game.suspects) {
            val correct = suspect == "Bob"
            println("You accused $suspect.")
            if (correct) println("Congratulations! You solved the mystery!")
            else println("Wrong! The real culprit was Bob.")
            game.withAccused(suspect)
        } else {
            println("Suspect not found.")
            game
        }
    }
}

object HelpCommand : Command {
    override fun execute(game: Game): Game {
        println("Available commands:")
        println("suspects - list the suspects")
        println("clues - show found and available clues")
        println("find <clue> - find a clue")
        println("accuse <suspect> - accuse a suspect")
        println("help - show this menu")
        println("exit - end the game")
        return game
    }
}

object ExitCommand : Command {
    override fun execute(game: Game): Game {
        println("Game ended.")
        return game.finish()
    }
}

object UnknownCommand : Command {
    override fun execute(game: Game): Game {
        println("Unknown command. Type 'help' to see the commands.")
        return game
    }
}
