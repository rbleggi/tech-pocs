package com.rbleggi.dontpad

import java.nio.file.{Files, Paths}
import org.scalatest.funsuite.AnyFunSuite

class DontPadSpec extends AnyFunSuite {
  test("NotePad should create new pad with empty content") {
    val pad = new NotePad("test-empty")
    assert(pad.getAllText.isEmpty || pad.getAllText == "")
    cleanupTestFile("test-empty")
  }

  test("NotePad should save and load text") {
    val pad = new NotePad("test-save-load")
    pad.setAllText("Hello World")

    val pad2 = new NotePad("test-save-load")
    assert(pad2.getAllText == "Hello World")

    cleanupTestFile("test-save-load")
  }

  test("NotePad should handle multiline text") {
    val pad = new NotePad("test-multiline")
    pad.setAllText("Line 1\nLine 2\nLine 3")

    val pad2 = new NotePad("test-multiline")
    assert(pad2.getAllText.contains("Line 1"))
    assert(pad2.getAllText.contains("Line 2"))
    assert(pad2.getAllText.contains("Line 3"))

    cleanupTestFile("test-multiline")
  }

  test("LoadNoteCommand should execute without error") {
    val pad = new NotePad("test-load-cmd")
    pad.setAllText("Test content")

    val cmd = new LoadNoteCommand(pad)
    assertNoException(cmd.execute())

    cleanupTestFile("test-load-cmd")
  }

  test("AppendNoteCommand should append text to existing note") {
    val pad = new NotePad("test-append")
    pad.setAllText("Original")

    val cmd = new AppendNoteCommand(pad, "Appended")
    cmd.execute()

    assert(pad.getAllText.contains("Original"))
    assert(pad.getAllText.contains("Appended"))

    cleanupTestFile("test-append")
  }

  test("AppendNoteCommand should create new note if empty") {
    val pad = new NotePad("test-append-new")

    val cmd = new AppendNoteCommand(pad, "First content")
    cmd.execute()

    assert(pad.getAllText == "First content")

    cleanupTestFile("test-append-new")
  }

  test("NoOpCommand should execute without error") {
    val cmd = new NoOpCommand()
    assertNoException(cmd.execute())
  }

  def assertNoException(block: => Unit): Unit = {
    try {
      block
      assert(true)
    } catch {
      case _: Exception => fail("Unexpected exception")
    }
  }

  def cleanupTestFile(key: String): Unit = {
    val path = Paths.get(s"notes_$key.txt")
    if (Files.exists(path)) {
      Files.delete(path)
    }
  }
}
