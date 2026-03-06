package com.rbleggi.multimodal

sealed trait InputType
case class TextInput(text: String) extends InputType
case class NumberInput(number: Double) extends InputType
case class CategoryInput(category: String) extends InputType

case class Analysis(inputType: String, result: String, confidence: Double)

trait ProcessorStrategy:
  def process(input: InputType): Analysis

class TextProcessor extends ProcessorStrategy:
  def process(input: InputType): Analysis =
    input match
      case TextInput(text) =>
        val words = text.split("\\s+").length
        val sentiment =
          if text.toLowerCase.contains("good") || text.toLowerCase.contains("great") then "positive"
          else if text.toLowerCase.contains("bad") || text.toLowerCase.contains("terrible") then "negative"
          else "neutral"
        Analysis("text", s"$words words, sentiment $sentiment", 0.85)
      case _ =>
        Analysis("text", "invalid input", 0.0)

class NumberProcessor extends ProcessorStrategy:
  def process(input: InputType): Analysis =
    input match
      case NumberInput(number) =>
        val category =
          if number < 0 then "negative"
          else if number == 0 then "zero"
          else if number < 100 then "small"
          else if number < 1000 then "medium"
          else "large"
        Analysis("number", s"value $number classified as $category", 0.95)
      case _ =>
        Analysis("number", "invalid input", 0.0)

class CategoryProcessor extends ProcessorStrategy:
  private val categories = Map(
    "sp" -> "Sao Paulo",
    "rj" -> "Rio de Janeiro",
    "mg" -> "Minas Gerais",
    "ba" -> "Bahia"
  )

  def process(input: InputType): Analysis =
    input match
      case CategoryInput(cat) =>
        val stateName = categories.get(cat.toLowerCase)
        stateName match
          case Some(name) => Analysis("category", s"state identified: $name", 1.0)
          case None => Analysis("category", "unknown state", 0.3)
      case _ =>
        Analysis("category", "invalid input", 0.0)

class MultiModalSystem:
  private var processors: Map[String, ProcessorStrategy] = Map(
    "text" -> TextProcessor(),
    "number" -> NumberProcessor(),
    "category" -> CategoryProcessor()
  )

  def process(input: InputType, mode: String): Analysis =
    processors.get(mode) match
      case Some(processor) => processor.process(input)
      case None => Analysis("error", "unknown mode", 0.0)

  def addProcessor(mode: String, processor: ProcessorStrategy): Unit =
    processors = processors + (mode -> processor)

@main def run(): Unit =
  println("Multi Modal AI System")
