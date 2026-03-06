package com.rbleggi.multimodal

import org.scalatest.funsuite.AnyFunSuite

class MultiModalSpec extends AnyFunSuite:
  test("TextProcessor should analyze positive text"):
    val processor = TextProcessor()
    val input = TextInput("The service is very good")
    val analysis = processor.process(input)

    assert(analysis.inputType == "text")
    assert(analysis.result.contains("positive"))
    assert(analysis.confidence > 0.8)

  test("TextProcessor should analyze negative text"):
    val processor = TextProcessor()
    val input = TextInput("The service was bad")
    val analysis = processor.process(input)

    assert(analysis.result.contains("negative"))

  test("TextProcessor should count words"):
    val processor = TextProcessor()
    val input = TextInput("Joao Maria Carlos")
    val analysis = processor.process(input)

    assert(analysis.result.contains("3 words"))

  test("NumberProcessor should classify small number"):
    val processor = NumberProcessor()
    val input = NumberInput(50.0)
    val analysis = processor.process(input)

    assert(analysis.inputType == "number")
    assert(analysis.result.contains("small"))

  test("NumberProcessor should classify large number"):
    val processor = NumberProcessor()
    val input = NumberInput(5000.0)
    val analysis = processor.process(input)

    assert(analysis.result.contains("large"))

  test("CategoryProcessor should identify SP"):
    val processor = CategoryProcessor()
    val input = CategoryInput("sp")
    val analysis = processor.process(input)

    assert(analysis.inputType == "category")
    assert(analysis.result.contains("Sao Paulo"))
    assert(analysis.confidence == 1.0)

  test("CategoryProcessor should identify unknown category"):
    val processor = CategoryProcessor()
    val input = CategoryInput("xyz")
    val analysis = processor.process(input)

    assert(analysis.result.contains("unknown"))

  test("MultiModalSystem should process text"):
    val system = MultiModalSystem()
    val input = TextInput("test")
    val analysis = system.process(input, "text")

    assert(analysis.inputType == "text")

  test("MultiModalSystem should process number"):
    val system = MultiModalSystem()
    val input = NumberInput(100.0)
    val analysis = system.process(input, "number")

    assert(analysis.inputType == "number")

  test("MultiModalSystem should return error for unknown mode"):
    val system = MultiModalSystem()
    val input = TextInput("test")
    val analysis = system.process(input, "invalid")

    assert(analysis.inputType == "error")
