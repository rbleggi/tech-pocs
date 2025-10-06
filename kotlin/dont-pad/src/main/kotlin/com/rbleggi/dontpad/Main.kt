package com.rbleggi.dontpad

class NotePad(val key: String) {
    private val notes: MutableList<String> = mutableListOf()

    fun getAllText(): String = notes.joinToString("\n")

    fun setAllText(text: String) {
        notes.clear()
        notes.addAll(text.split("\n"))
    }

    fun appendText(text: String) {
        notes.add(text)
    }
}

interface Command { fun execute() }

class LoadNoteCommand(private val notePad: NotePad) : Command {
    override fun execute() {
        println(notePad.getAllText())
    }
}

class AppendNoteCommand(private val notePad: NotePad, private val text: String) : Command {
    override fun execute() {
        notePad.appendText(text)
    }
}

class NoOpCommand : Command {
    override fun execute() { /* No operation */ }
}

fun main() {
    val key = "/mypage"
    val notePad = NotePad(key)

    println("[LOG] Creating NotePad for page: $key")

    val appendCommand = AppendNoteCommand(notePad, "Hello, world!")
    println("[LOG] Executing AppendNoteCommand: adding 'Hello, world!'")
    appendCommand.execute()

    val loadCommand = LoadNoteCommand(notePad)
    println("[LOG] Executing LoadNoteCommand: displaying current content")
    loadCommand.execute()

    val noOpCommand = NoOpCommand()
    println("[LOG] Executing NoOpCommand: no operation will be performed")
    noOpCommand.execute()
}
