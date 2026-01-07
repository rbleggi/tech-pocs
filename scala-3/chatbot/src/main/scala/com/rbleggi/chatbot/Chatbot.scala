package com.rbleggi.chatbot

import scala.collection.mutable

case class ConversationContext(
  userName: Option[String] = None,
  history: List[String] = List.empty,
  entities: Map[String, String] = Map.empty
):
  def withUserName(name: String): ConversationContext = copy(userName = Some(name))
  def addToHistory(message: String): ConversationContext = copy(history = message :: history)
  def addEntity(key: String, value: String): ConversationContext =
    copy(entities = entities + (key -> value))

trait Command:
  def execute(input: String, context: ConversationContext): (String, ConversationContext)
  def matches(input: String): Boolean

class GreetingCommand extends Command:
  private val greetingPatterns = List("hello", "hi", "hey", "greetings", "good morning", "good afternoon")

  override def matches(input: String): Boolean =
    greetingPatterns.exists(pattern => input.toLowerCase.contains(pattern))

  override def execute(input: String, context: ConversationContext): (String, ConversationContext) =
    val extractedName = extractName(input)
    val (response, newContext) = extractedName match
      case Some(name) =>
        val ctx = context.withUserName(name)
        (s"Hello, $name! How can I help you today?", ctx)
      case None if context.userName.isDefined =>
        (s"Hello again, ${context.userName.get}! What can I do for you?", context)
      case None =>
        ("Hello! I'm your AI assistant. What's your name?", context)

    (response, newContext.addToHistory(input))

  private def extractName(input: String): Option[String] =
    val namePattern = """(?i)(?:my name is|i'm|i am|call me)\s+([a-zA-Z]+)""".r
    namePattern.findFirstMatchIn(input).map(_.group(1))
