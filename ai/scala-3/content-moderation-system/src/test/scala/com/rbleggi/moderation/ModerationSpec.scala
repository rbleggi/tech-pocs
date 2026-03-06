package com.rbleggi.moderation

import org.scalatest.funsuite.AnyFunSuite

class ModerationSpec extends AnyFunSuite:
  test("KeywordFilter should block spam"):
    val filter = KeywordFilter()
    val content = Content("1", "This is cheap spam content", "Joao")
    val result = filter.moderate(content)

    assert(!result.approved)
    assert(result.reason.contains("spam"))
    assert(result.severity == "high")

  test("KeywordFilter should block golpe"):
    val filter = KeywordFilter()
    val content = Content("2", "Come participate in this golpe", "Maria")
    val result = filter.moderate(content)

    assert(!result.approved)
    assert(result.reason.contains("golpe"))

  test("KeywordFilter should approve normal text"):
    val filter = KeywordFilter()
    val content = Content("3", "Completely normal text content", "Carlos")
    val result = filter.moderate(content)

    assert(result.approved)

  test("RegexFilter should detect CPF"):
    val filter = RegexFilter()
    val content = Content("4", "My CPF is 123.456.789-00", "Ana")
    val result = filter.moderate(content)

    assert(!result.approved)
    assert(result.reason.contains("sensitive data"))

  test("RegexFilter should detect BRL value"):
    val filter = RegexFilter()
    val content = Content("5", "The price is R$ 150", "Pedro")
    val result = filter.moderate(content)

    assert(!result.approved)

  test("RegexFilter should approve text without sensitive data"):
    val filter = RegexFilter()
    val content = Content("6", "Message without personal information", "Lucas")
    val result = filter.moderate(content)

    assert(result.approved)

  test("LengthFilter should block short text"):
    val filter = LengthFilter()
    val content = Content("7", "Hi", "Joao")
    val result = filter.moderate(content)

    assert(!result.approved)
    assert(result.reason.contains("too short"))

  test("LengthFilter should approve adequate text"):
    val filter = LengthFilter()
    val content = Content("8", "Text with adequate length for moderation", "Maria")
    val result = filter.moderate(content)

    assert(result.approved)

  test("ModerationSystem should approve valid content"):
    val system = ModerationSystem()
    val content = Content("9", "Valid message with adequate length", "Carlos")

    assert(system.approve(content))

  test("ModerationSystem should reject content with spam"):
    val system = ModerationSystem()
    val content = Content("10", "This is spam and golpe content", "Ana")

    assert(!system.approve(content))
