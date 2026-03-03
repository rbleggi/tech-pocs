package com.rbleggi.filesharesystem

import scala.collection.mutable

case class File(name: String, content: String, isEncrypted: Boolean = false)

class FileManager:
  private val files = mutable.Map[String, File]()

  def saveFile(file: File): Unit =
    files(file.name) = file
    println(s"File '${file.name}' saved.")

  def restoreFile(file: File): Unit =
    if files.contains(file.name) then println(s"File '${file.name}' restored.")
    else println(s"File '${file.name}' does not exist.")

  def deleteFile(file: File): Unit =
    if files.remove(file.name).isDefined then println(s"File '${file.name}' deleted.")
    else println(s"File '${file.name}' does not exist.")

  def listFiles(): Unit =
    if files.isEmpty then println("\nNo files available.")
    else
      println("\nFiles:")
      files.keys.foreach(println)

  def searchFile(query: String): Unit =
    val results = files.keys.filter(_.contains(query))
    if results.isEmpty then println(s"\nNo files found matching '$query'.")
    else
      println(s"\nFiles matching '$query':")
      results.foreach(println)

trait Command:
  def execute(): Unit
  def undo(): Unit

case class SaveFileCommand(fileManager: FileManager, file: File) extends Command:
  override def execute(): Unit = fileManager.saveFile(file)
  override def undo(): Unit = fileManager.deleteFile(file)

case class RestoreFileCommand(fileManager: FileManager, file: File) extends Command:
  override def execute(): Unit = fileManager.restoreFile(file)
  override def undo(): Unit = fileManager.deleteFile(file)

case class DeleteFileCommand(fileManager: FileManager, file: File) extends Command:
  override def execute(): Unit = fileManager.deleteFile(file)
  override def undo(): Unit = fileManager.saveFile(file)

case class ListFilesCommand(fileManager: FileManager) extends Command:
  override def execute(): Unit = fileManager.listFiles()
  override def undo(): Unit = println("Undo not supported for listing files.")

case class SearchFileCommand(fileManager: FileManager, query: String) extends Command:
  override def execute(): Unit = fileManager.searchFile(query)
  override def undo(): Unit = println("Undo not supported for searching files.")

class CommandInvoker:
  private val history = mutable.Stack[Command]()
  private val redoStack = mutable.Stack[Command]()

  def executeCommand(command: Command): Unit =
    command.execute()
    history.push(command)
    redoStack.clear()

  def undo(): Unit =
    if history.nonEmpty then
      val command = history.pop()
      command.undo()
      redoStack.push(command)
    else println("No actions to undo.")

  def redo(): Unit =
    if redoStack.nonEmpty then
      val command = redoStack.pop()
      command.execute()
      history.push(command)
    else println("No actions to redo.")

@main def run(): Unit =
  println("File Share")
