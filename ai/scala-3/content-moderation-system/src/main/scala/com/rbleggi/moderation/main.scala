package com.rbleggi.moderation

case class Content(id: String, text: String, author: String)

case class ModerationResult(approved: Boolean, reason: String, severity: String)

trait ModerationStrategy:
  def moderate(content: Content): ModerationResult

class KeywordFilter extends ModerationStrategy:
  private val bannedWords = Set(
    "spam", "golpe", "fraude", "scam"
  )

  def moderate(content: Content): ModerationResult =
    val lowerText = content.text.toLowerCase
    val found = bannedWords.filter(w => lowerText.contains(w))

    if found.nonEmpty then
      ModerationResult(false, s"banned words found: ${found.mkString(", ")}", "high")
    else
      ModerationResult(true, "no banned words found", "low")

class RegexFilter extends ModerationStrategy:
  private val patterns = List(
    "\\d{3}\\.\\d{3}\\.\\d{3}-\\d{2}".r,
    "\\d{11}".r,
    "R\\$\\s*\\d+".r
  )

  def moderate(content: Content): ModerationResult =
    val matches = patterns.flatMap(p => p.findAllIn(content.text))

    if matches.nonEmpty then
      ModerationResult(false, "sensitive data detected", "medium")
    else
      ModerationResult(true, "no sensitive data found", "low")

class LengthFilter extends ModerationStrategy:
  private val minLength = 10
  private val maxLength = 500

  def moderate(content: Content): ModerationResult =
    val length = content.text.length

    if length < minLength then
      ModerationResult(false, s"text too short ($length characters)", "low")
    else if length > maxLength then
      ModerationResult(false, s"text too long ($length characters)", "medium")
    else
      ModerationResult(true, "adequate length", "low")

class ModerationSystem:
  private var filters: List[ModerationStrategy] = List(
    KeywordFilter(),
    RegexFilter(),
    LengthFilter()
  )

  def moderateContent(content: Content): List[ModerationResult] =
    filters.map(f => f.moderate(content))

  def approve(content: Content): Boolean =
    val results = moderateContent(content)
    results.forall(_.approved)

  def addFilter(filter: ModerationStrategy): Unit =
    filters = filters :+ filter

@main def run(): Unit =
  println("Content Moderation System")
