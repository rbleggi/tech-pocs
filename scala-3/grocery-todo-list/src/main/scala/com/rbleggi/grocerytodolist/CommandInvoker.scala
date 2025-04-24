package com.rbleggi.grocerytodolist

object CommandInvoker {
  private var history: List[Command] = List.empty
  private var redoStack: List[Command] = List.empty

  def executeCommand(command: Command): Boolean = {
    val result = command.execute()
    if (result) {
      history = command :: history
      redoStack = List.empty
    }
    result
  }

  def undo(): Boolean =
    history match {
      case cmd :: rest =>
        val result = cmd.undo()
        if (result) {
          history = rest
          redoStack = cmd :: redoStack
          println("Undo successful")
        } else {
          println("Undo failed")
        }
        result
      case Nil =>
        println("Nothing to undo")
        false
    }

  def redo(): Boolean =
    redoStack match {
      case cmd :: rest =>
        val result = cmd.execute()
        if (result) {
          redoStack = rest
          history = cmd :: history
          println("Redo successful")
        } else {
          println("Redo failed")
        }
        result
      case Nil =>
        println("Nothing to redo")
        false
    }
}
