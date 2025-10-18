package com.rbleggi.notetaking

import java.io.File

data class Note(val id: Int, val title: String, val content: String)

class NoteManager {
    private val notes = mutableMapOf<Int, Note>()
    private var nextId = 1

    fun addNote(title: String, content: String): Note {
        val note = Note(nextId, title, content)
        notes[nextId] = note
        nextId++
        return note
    }

    fun editNote(id: Int, newTitle: String? = null, newContent: String? = null): Note? {
        return notes[id]?.let { note ->
            val updatedNote = note.copy(
                title = newTitle ?: note.title,
                content = newContent ?: note.content
            )
            notes[id] = updatedNote
            updatedNote
        }
    }

    fun deleteNote(id: Int): Boolean = notes.remove(id) != null

    fun listNotes(): List<Note> = notes.values.toList()

    fun saveNotes(filePath: String) {
        File(filePath).bufferedWriter().use { writer ->
            notes.values.forEach { note ->
                writer.write("${note.id}|${note.title}|${note.content}\n")
            }
        }
    }

    fun loadNotes(filePath: String) {
        val file = File(filePath)
        if (file.exists()) {
            file.forEachLine { line ->
                val parts = line.split("|", limit = 3)
                if (parts.size == 3) {
                    val (id, title, content) = parts
                    notes[id.toInt()] = Note(id.toInt(), title, content)
                    nextId = maxOf(nextId, id.toInt() + 1)
                }
            }
        }
    }
}

interface Command {
    fun execute()
}

class AddNoteCommand(
    private val manager: NoteManager,
    private val title: String,
    private val content: String
) : Command {
    override fun execute() {
        manager.addNote(title, content)
    }
}

class EditNoteCommand(
    private val manager: NoteManager,
    private val id: Int,
    private val newTitle: String? = null,
    private val newContent: String? = null
) : Command {
    override fun execute() {
        manager.editNote(id, newTitle, newContent)
    }
}

class DeleteNoteCommand(
    private val manager: NoteManager,
    private val id: Int
) : Command {
    override fun execute() {
        manager.deleteNote(id)
    }
}

class SaveNotesCommand(
    private val manager: NoteManager,
    private val filePath: String
) : Command {
    override fun execute() {
        manager.saveNotes(filePath)
    }
}

class LoadNotesCommand(
    private val manager: NoteManager,
    private val filePath: String
) : Command {
    override fun execute() {
        manager.loadNotes(filePath)
    }
}

class CommandManager {
    private val history = mutableListOf<Command>()

    fun executeCommand(command: Command) {
        command.execute()
        history.add(command)
    }
}

fun main() {
    val manager = NoteManager()
    val commandManager = CommandManager()

    commandManager.executeCommand(AddNoteCommand(manager, "First Note", "This is the content of the first note."))
    commandManager.executeCommand(AddNoteCommand(manager, "Second Note", "This is the content of the second note."))
    commandManager.executeCommand(SaveNotesCommand(manager, "notes.txt"))
    println("Notes saved to file.")

    commandManager.executeCommand(LoadNotesCommand(manager, "notes.txt"))
    println("Notes loaded from file:")
    manager.listNotes().forEach(::println)
}
