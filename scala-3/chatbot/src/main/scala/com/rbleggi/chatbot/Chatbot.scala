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

class WeatherCommand extends Command:
  private val weatherPatterns = List("weather", "temperature", "forecast", "rain", "sunny")

  override def matches(input: String): Boolean =
    weatherPatterns.exists(pattern => input.toLowerCase.contains(pattern))

  override def execute(input: String, context: ConversationContext): (String, ConversationContext) =
    val city = extractCity(input)
    val response = city match
      case Some(c) =>
        s"The weather in $c is currently sunny with a temperature of 72°F. (This is simulated data)"
      case None =>
        "I can check the weather for you! Which city are you interested in?"

    val newContext = city.map(c => context.addEntity("city", c)).getOrElse(context)
    (response, newContext.addToHistory(input))

  private def extractCity(input: String): Option[String] =
    val cityPattern = """(?i)(?:in|at|for)\s+([A-Z][a-zA-Z\s]+)(?:\?|$)""".r
    cityPattern.findFirstMatchIn(input).map(_.group(1).trim)

class TimeCommand extends Command:
  private val timePatterns = List("time", "clock", "what time")

  override def matches(input: String): Boolean =
    timePatterns.exists(pattern => input.toLowerCase.contains(pattern))

  override def execute(input: String, context: ConversationContext): (String, ConversationContext) =
    val currentTime = java.time.LocalTime.now()
    val response = s"The current time is ${currentTime.toString.take(8)}"
    (response, context.addToHistory(input))

class ReminderCommand extends Command:
  private val reminderPatterns = List("remind", "reminder", "schedule")

  override def matches(input: String): Boolean =
    reminderPatterns.exists(pattern => input.toLowerCase.contains(pattern))

  override def execute(input: String, context: ConversationContext): (String, ConversationContext) =
    val task = extractTask(input)
    val response = task match
      case Some(t) =>
        s"I'll remind you to: $t. (Reminder set successfully)"
      case None =>
        "What would you like me to remind you about?"

    val newContext = task.map(t => context.addEntity("reminder", t)).getOrElse(context)
    (response, newContext.addToHistory(input))

  private def extractTask(input: String): Option[String] =
    val taskPattern = """(?i)remind me to\s+(.+)""".r
    taskPattern.findFirstMatchIn(input).map(_.group(1).trim)
