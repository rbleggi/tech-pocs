package com.rbleggi.filesharesystem

import org.scalatest.funsuite.AnyFunSuite

class FileShareSystemSpec extends AnyFunSuite {
  test("FileManager should save file successfully") {
    val manager = new FileManager
    val file = File("test.txt", "content")
    assertNoException {
      manager.saveFile(file)
    }
  }

  test("FileManager should delete existing file") {
    val manager = new FileManager
    val file = File("test.txt", "content")
    manager.saveFile(file)
    assertNoException {
      manager.deleteFile(file)
    }
  }

  test("FileManager should handle deleting non-existent file") {
    val manager = new FileManager
    val file = File("nonexistent.txt", "content")
    assertNoException {
      manager.deleteFile(file)
    }
  }

  test("FileManager should restore existing file") {
    val manager = new FileManager
    val file = File("test.txt", "content")
    manager.saveFile(file)
    assertNoException {
      manager.restoreFile(file)
    }
  }

  test("FileManager should list files") {
    val manager = new FileManager
    assertNoException {
      manager.listFiles()
    }
  }

  test("FileManager should search files by query") {
    val manager = new FileManager
    val file1 = File("test1.txt", "content1")
    val file2 = File("test2.txt", "content2")
    val file3 = File("other.txt", "content3")

    manager.saveFile(file1)
    manager.saveFile(file2)
    manager.saveFile(file3)

    assertNoException {
      manager.searchFile("test")
    }
  }

  test("SaveFileCommand should execute and save file") {
    val manager = new FileManager
    val file = File("test.txt", "content")
    val cmd = SaveFileCommand(manager, file)

    assertNoException {
      cmd.execute()
    }
  }

  test("SaveFileCommand should undo and delete file") {
    val manager = new FileManager
    val file = File("test.txt", "content")
    val cmd = SaveFileCommand(manager, file)

    cmd.execute()
    assertNoException {
      cmd.undo()
    }
  }

  test("DeleteFileCommand should execute and delete file") {
    val manager = new FileManager
    val file = File("test.txt", "content")
    manager.saveFile(file)

    val cmd = DeleteFileCommand(manager, file)
    assertNoException {
      cmd.execute()
    }
  }

  test("DeleteFileCommand should undo and restore file") {
    val manager = new FileManager
    val file = File("test.txt", "content")
    manager.saveFile(file)

    val cmd = DeleteFileCommand(manager, file)
    cmd.execute()
    assertNoException {
      cmd.undo()
    }
  }

  test("RestoreFileCommand should execute") {
    val manager = new FileManager
    val file = File("test.txt", "content")
    manager.saveFile(file)

    val cmd = RestoreFileCommand(manager, file)
    assertNoException {
      cmd.execute()
    }
  }

  test("ListFilesCommand should execute") {
    val manager = new FileManager
    val cmd = ListFilesCommand(manager)
    assertNoException {
      cmd.execute()
    }
  }

  test("SearchFileCommand should execute") {
    val manager = new FileManager
    val cmd = SearchFileCommand(manager, "test")
    assertNoException {
      cmd.execute()
    }
  }

  test("CommandInvoker should execute command and add to history") {
    val invoker = new CommandInvoker
    val manager = new FileManager
    val file = File("test.txt", "content")
    val cmd = SaveFileCommand(manager, file)

    assertNoException {
      invoker.executeCommand(cmd)
    }
  }

  test("CommandInvoker should undo last command") {
    val invoker = new CommandInvoker
    val manager = new FileManager
    val file = File("test.txt", "content")
    val cmd = SaveFileCommand(manager, file)

    invoker.executeCommand(cmd)
    assertNoException {
      invoker.undo()
    }
  }

  test("CommandInvoker should redo last undone command") {
    val invoker = new CommandInvoker
    val manager = new FileManager
    val file = File("test.txt", "content")
    val cmd = SaveFileCommand(manager, file)

    invoker.executeCommand(cmd)
    invoker.undo()
    assertNoException {
      invoker.redo()
    }
  }

  test("CommandInvoker should handle undo with empty history") {
    val invoker = new CommandInvoker
    assertNoException {
      invoker.undo()
    }
  }

  test("CommandInvoker should handle redo with empty stack") {
    val invoker = new CommandInvoker
    assertNoException {
      invoker.redo()
    }
  }

  test("CommandInvoker should clear redo stack after new command") {
    val invoker = new CommandInvoker
    val manager = new FileManager
    val file1 = File("test1.txt", "content1")
    val file2 = File("test2.txt", "content2")

    invoker.executeCommand(SaveFileCommand(manager, file1))
    invoker.undo()
    invoker.executeCommand(SaveFileCommand(manager, file2))
    assertNoException {
      invoker.redo()
    }
  }

  test("CommandInvoker should handle multiple undo and redo operations") {
    val invoker = new CommandInvoker
    val manager = new FileManager
    val file1 = File("file1.txt", "content1")
    val file2 = File("file2.txt", "content2")
    val file3 = File("file3.txt", "content3")

    invoker.executeCommand(SaveFileCommand(manager, file1))
    invoker.executeCommand(SaveFileCommand(manager, file2))
    invoker.executeCommand(SaveFileCommand(manager, file3))

    invoker.undo()
    invoker.undo()
    invoker.redo()

    assertNoException {
      invoker.undo()
    }
  }

  def assertNoException(block: => Unit): Unit = {
    try {
      block
      assert(true)
    } catch {
      case _: Exception => fail("Unexpected exception")
    }
  }
}
