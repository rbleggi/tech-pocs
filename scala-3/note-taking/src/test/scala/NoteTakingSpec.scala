package com.rbleggi.notetaking

import org.scalatest.funsuite.AnyFunSuite

class NoteTakingSpec extends AnyFunSuite {
  test("NoteManager add, edit, delete, and list notes") {
    val manager = new NoteManager
    val note1 = manager.addNote("Title1", "Content1")
    val note2 = manager.addNote("Title2", "Content2")
    assert(manager.listNotes().size == 2)
    assert(manager.listNotes().exists(_.title == "Title1"))
    val edited = manager.editNote(note1.id, Some("NewTitle1"), None)
    assert(edited.exists(_.title == "NewTitle1"))
    assert(manager.deleteNote(note2.id))
    assert(manager.listNotes().size == 1)
  }

  test("NoteManager should add note with correct title and content") {
    val manager = new NoteManager
    val note = manager.addNote("Test Title", "Test Content")
    assert(note.title == "Test Title")
    assert(note.content == "Test Content")
    assert(note.id > 0)
  }

  test("NoteManager should edit only title") {
    val manager = new NoteManager
    val note = manager.addNote("Original", "Content")
    val edited = manager.editNote(note.id, Some("Updated"), None)
    assert(edited.exists(_.title == "Updated"))
    assert(edited.exists(_.content == "Content"))
  }

  test("NoteManager should edit only content") {
    val manager = new NoteManager
    val note = manager.addNote("Title", "Original")
    val edited = manager.editNote(note.id, None, Some("Updated"))
    assert(edited.exists(_.title == "Title"))
    assert(edited.exists(_.content == "Updated"))
  }

  test("NoteManager should edit both title and content") {
    val manager = new NoteManager
    val note = manager.addNote("Old Title", "Old Content")
    val edited = manager.editNote(note.id, Some("New Title"), Some("New Content"))
    assert(edited.exists(_.title == "New Title"))
    assert(edited.exists(_.content == "New Content"))
  }

  test("NoteManager should return None when editing non-existent note") {
    val manager = new NoteManager
    val edited = manager.editNote(9999, Some("Title"), Some("Content"))
    assert(edited.isEmpty)
  }

  test("NoteManager should not delete non-existent note") {
    val manager = new NoteManager
    assert(!manager.deleteNote(9999))
  }

  test("NoteManager should list all notes") {
    val manager = new NoteManager
    manager.addNote("Title1", "Content1")
    manager.addNote("Title2", "Content2")
    manager.addNote("Title3", "Content3")
    val notes = manager.listNotes()
    assert(notes.size == 3)
  }

  test("NoteManager should return empty list when no notes") {
    val manager = new NoteManager
    assert(manager.listNotes().isEmpty)
  }

  test("NoteManager should generate unique IDs for notes") {
    val manager = new NoteManager
    val note1 = manager.addNote("Title1", "Content1")
    val note2 = manager.addNote("Title2", "Content2")
    assert(note1.id != note2.id)
  }
}
