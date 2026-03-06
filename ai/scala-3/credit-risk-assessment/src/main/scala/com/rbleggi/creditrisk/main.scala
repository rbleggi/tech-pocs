package com.rbleggi.creditrisk

enum RiskLevel:
  case Low, Medium, High, VeryHigh

case class Client(
  name: String,
  cpf: String,
  age: Int,
  salary: Double,
  creditScore: Int,
  debts: Double,
  employmentTime: Int
)

case class RiskAssessment(client: Client, level: RiskLevel, score: Double, reason: String)

trait RiskAssessmentStrategy:
  def assess(client: Client): (RiskLevel, Double, String)

class ScoreBasedStrategy extends RiskAssessmentStrategy:
  def assess(client: Client): (RiskLevel, Double, String) =
    val score = client.creditScore.toDouble

    val (level, reason) = if score >= 800 then
      (RiskLevel.Low, "Excellent credit score")
    else if score >= 650 then
      (RiskLevel.Medium, "Good credit score")
    else if score >= 500 then
      (RiskLevel.High, "Regular credit score")
    else
      (RiskLevel.VeryHigh, "Low credit score")

    (level, score / 10.0, reason)

class DebtToIncomeStrategy extends RiskAssessmentStrategy:
  def assess(client: Client): (RiskLevel, Double, String) =
    val ratio = if client.salary > 0 then
      (client.debts / client.salary) * 100
    else
      100.0

    val score = Math.max(0, 100 - ratio)

    val (level, reason) = if ratio <= 20 then
      (RiskLevel.Low, f"Debts only ${ratio}%.1f%% of income")
    else if ratio <= 40 then
      (RiskLevel.Medium, f"Debts ${ratio}%.1f%% of income")
    else if ratio <= 60 then
      (RiskLevel.High, f"Debts ${ratio}%.1f%% of income - high commitment")
    else
      (RiskLevel.VeryHigh, f"Debts ${ratio}%.1f%% of income - critical commitment")

    (level, score, reason)

class CompositeStrategy extends RiskAssessmentStrategy:
  def assess(client: Client): (RiskLevel, Double, String) =
    var points = 100.0
    val reasons = scala.collection.mutable.ListBuffer[String]()

    if client.creditScore < 500 then
      points -= 30
      reasons += "Low score"
    else if client.creditScore < 650 then
      points -= 15
      reasons += "Moderate score"

    val debtRatio = if client.salary > 0 then
      (client.debts / client.salary) * 100
    else
      100.0

    if debtRatio > 60 then
      points -= 25
      reasons += "High debt"
    else if debtRatio > 40 then
      points -= 15
      reasons += "Moderate debt"

    if client.salary < 2000 then
      points -= 20
      reasons += "Low salary"
    else if client.salary < 5000 then
      points -= 10

    if client.employmentTime < 6 then
      points -= 15
      reasons += "Short employment time"
    else if client.employmentTime < 12 then
      points -= 5

    if client.age < 21 then
      points -= 10
      reasons += "Young age"

    val level = if points >= 80 then RiskLevel.Low
    else if points >= 60 then RiskLevel.Medium
    else if points >= 40 then RiskLevel.High
    else RiskLevel.VeryHigh

    val finalReason = if reasons.isEmpty then "Adequate profile" else reasons.mkString(", ")

    (level, points, finalReason)

class RiskAssessor(strategy: RiskAssessmentStrategy):
  def assess(client: Client): RiskAssessment =
    val (level, score, reason) = strategy.assess(client)
    RiskAssessment(client, level, score, reason)

  def assessBatch(clients: List[Client]): List[RiskAssessment] =
    clients.map(assess)

@main def run(): Unit =
  println("Credit Risk Assessment")
