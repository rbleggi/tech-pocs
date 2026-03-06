package com.rbleggi.assistant

case class Query(queryType: String, question: String)

case class Response(queryType: String, answer: String, confidence: Double)

trait QueryHandler:
  def process(query: Query): Response

class MathHandler extends QueryHandler:
  def process(query: Query): Response =
    val question = query.question.toLowerCase
    try
      if question.contains("sum") || question.contains("+") then
        val numbers = question.split("\\D+").filter(_.nonEmpty).map(_.toInt)
        if numbers.length >= 2 then
          val sum = numbers.sum
          Response("math", s"The sum is $sum", 1.0)
        else
          Response("math", "Not enough numbers found", 0.3)
      else if question.contains("multiply") || question.contains("*") then
        val numbers = question.split("\\D+").filter(_.nonEmpty).map(_.toInt)
        if numbers.length >= 2 then
          val product = numbers.product
          Response("math", s"The product is $product", 1.0)
        else
          Response("math", "Not enough numbers found", 0.3)
      else
        Response("math", "Operation not recognized", 0.2)
    catch
      case _: Exception => Response("math", "Error processing numbers", 0.1)

class TextHandler extends QueryHandler:
  def process(query: Query): Response =
    val question = query.question.toLowerCase
    if question.contains("how many words") then
      val words = query.question.split("\\s+").length
      Response("text", s"The question has $words words", 0.9)
    else if question.contains("uppercase") then
      val uppercase = query.question.toUpperCase
      Response("text", uppercase, 1.0)
    else if question.contains("lowercase") then
      val lowercase = query.question.toLowerCase
      Response("text", lowercase, 1.0)
    else
      Response("text", "Text operation not recognized", 0.3)

class DataHandler extends QueryHandler:
  private val data = Map(
    "sao paulo" -> "Population: 12 million, state: SP",
    "curitiba" -> "Population: 1.9 million, state: PR",
    "belo horizonte" -> "Population: 2.5 million, state: MG"
  )

  def process(query: Query): Response =
    val question = query.question.toLowerCase
    val foundCity = data.keys.find(c => question.contains(c))

    foundCity match
      case Some(city) =>
        Response("data", data(city), 0.95)
      case None =>
        Response("data", "City not found in database", 0.2)

class PersonalAssistant:
  private val handlers = Map(
    "math" -> MathHandler(),
    "text" -> TextHandler(),
    "data" -> DataHandler()
  )

  def query(query: Query): Response =
    handlers.get(query.queryType) match
      case Some(handler) => handler.process(query)
      case None => Response("error", "Query type not supported", 0.0)

  def autoQuery(question: String): Response =
    val questionLower = question.toLowerCase
    val queryType =
      if questionLower.matches(".*\\d+.*") && (questionLower.contains("sum") || questionLower.contains("multiply")) then
        "math"
      else if questionLower.contains("uppercase") || questionLower.contains("lowercase") || questionLower.contains("words") then
        "text"
      else if questionLower.contains("sao paulo") || questionLower.contains("curitiba") || questionLower.contains("belo horizonte") then
        "data"
      else
        "unknown"
    query(Query(queryType, question))

@main def run(): Unit =
  println("Personal AI Assistant")
