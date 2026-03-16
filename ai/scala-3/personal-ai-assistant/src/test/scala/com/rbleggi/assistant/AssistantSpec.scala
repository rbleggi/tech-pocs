package com.rbleggi.assistant

import org.scalatest.funsuite.AnyFunSuite

class AssistantSpec extends AnyFunSuite:
  test("MathHandler should sum numbers"):
    val handler = MathHandler()
    val q = Query("math", "What is the sum of 10 and 20?")
    val response = handler.process(q)

    assert(response.queryType == "math")
    assert(response.answer.contains("30"))
    assert(response.confidence == 1.0)

  test("MathHandler should multiply numbers"):
    val handler = MathHandler()
    val q = Query("math", "Multiply 5 and 4")
    val response = handler.process(q)

    assert(response.answer.contains("20"))

  test("MathHandler should handle error"):
    val handler = MathHandler()
    val q = Query("math", "sum without numbers")
    val response = handler.process(q)

    assert(response.confidence < 1.0)

  test("TextHandler should count words"):
    val handler = TextHandler()
    val q = Query("text", "How many words in this sentence?")
    val response = handler.process(q)

    assert(response.queryType == "text")
    assert(response.answer.contains("words"))

  test("TextHandler should convert to uppercase"):
    val handler = TextHandler()
    val q = Query("text", "Convert to uppercase: joao")
    val response = handler.process(q)

    assert(response.answer.contains("JOAO"))
    assert(response.confidence == 1.0)

  test("TextHandler should convert to lowercase"):
    val handler = TextHandler()
    val q = Query("text", "Convert to lowercase: MARIA")
    val response = handler.process(q)

    assert(response.answer.toLowerCase.contains("maria"))

  test("DataHandler should find Sao Paulo"):
    val handler = DataHandler()
    val q = Query("data", "Information about sao paulo")
    val response = handler.process(q)

    assert(response.queryType == "data")
    assert(response.answer.contains("12 million"))
    assert(response.confidence > 0.9)

  test("DataHandler should find Curitiba"):
    val handler = DataHandler()
    val q = Query("data", "What do you know about curitiba?")
    val response = handler.process(q)

    assert(response.answer.contains("1.9 million"))

  test("DataHandler should return not found"):
    val handler = DataHandler()
    val q = Query("data", "Information about xyz")
    val response = handler.process(q)

    assert(response.answer.contains("not found"))

  test("PersonalAssistant should process math query"):
    val assistant = PersonalAssistant()
    val q = Query("math", "sum 15 and 25")
    val response = assistant.query(q)

    assert(response.queryType == "math")

  test("PersonalAssistant should process text query"):
    val assistant = PersonalAssistant()
    val q = Query("text", "How many words are here")
    val response = assistant.query(q)

    assert(response.queryType == "text")
