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
}
