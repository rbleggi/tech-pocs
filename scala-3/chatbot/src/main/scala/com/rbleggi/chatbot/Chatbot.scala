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
    val response = context.userName match
      case Some(name) => s"Hello again, $name! What can I do for you?"
      case None => "Hello! I'm your AI assistant. What's your name?"

    (response, context.addToHistory(input))
