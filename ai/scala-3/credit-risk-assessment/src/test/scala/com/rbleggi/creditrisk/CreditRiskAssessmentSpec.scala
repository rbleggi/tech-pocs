package com.rbleggi.creditrisk

import org.scalatest.funsuite.AnyFunSuite

class CreditRiskAssessmentSpec extends AnyFunSuite:

  test("ScoreBasedStrategy should classify high score as low risk"):
    val strategy = ScoreBasedStrategy()
    val client = Client("Joao", "123.456.789-01", 30, 5000.0, 850, 1000.0, 24)
    val (level, _, _) = strategy.assess(client)
    assert(level == RiskLevel.Low)

  test("ScoreBasedStrategy should classify good score as medium risk"):
    val strategy = ScoreBasedStrategy()
    val client = Client("Maria", "987.654.321-00", 28, 5000.0, 700, 1000.0, 24)
    val (level, _, _) = strategy.assess(client)
    assert(level == RiskLevel.Medium)

  test("ScoreBasedStrategy should classify regular score as high risk"):
    val strategy = ScoreBasedStrategy()
    val client = Client("Carlos", "111.222.333-44", 35, 5000.0, 550, 1000.0, 24)
    val (level, _, _) = strategy.assess(client)
    assert(level == RiskLevel.High)

  test("ScoreBasedStrategy should classify low score as very high risk"):
    val strategy = ScoreBasedStrategy()
    val client = Client("Ana", "555.666.777-88", 32, 5000.0, 400, 1000.0, 24)
    val (level, _, _) = strategy.assess(client)
    assert(level == RiskLevel.VeryHigh)

  test("DebtToIncomeStrategy should classify low debt as low risk"):
    val strategy = DebtToIncomeStrategy()
    val client = Client("Pedro", "999.888.777-66", 40, 10000.0, 700, 1500.0, 36)
    val (level, _, _) = strategy.assess(client)
    assert(level == RiskLevel.Low)

  test("DebtToIncomeStrategy should classify medium debt as medium risk"):
    val strategy = DebtToIncomeStrategy()
    val client = Client("Lucia", "123.123.123-12", 35, 5000.0, 700, 1800.0, 24)
    val (level, _, _) = strategy.assess(client)
    assert(level == RiskLevel.Medium)

  test("DebtToIncomeStrategy should classify high debt as high risk"):
    val strategy = DebtToIncomeStrategy()
    val client = Client("Roberto", "456.456.456-45", 30, 3000.0, 700, 1800.0, 24)
    val (level, _, _) = strategy.assess(client)
    assert(level == RiskLevel.High)

  test("DebtToIncomeStrategy should classify very high debt as very high risk"):
    val strategy = DebtToIncomeStrategy()
    val client = Client("Fernanda", "789.789.789-78", 28, 2000.0, 700, 1500.0, 24)
    val (level, _, _) = strategy.assess(client)
    assert(level == RiskLevel.VeryHigh)

  test("CompositeStrategy should combine multiple factors"):
    val strategy = CompositeStrategy()
    val client = Client("Joana", "321.321.321-32", 40, 10000.0, 850, 1000.0, 60)
    val (level, score, _) = strategy.assess(client)
    assert(level == RiskLevel.Low)
    assert(score >= 80)

  test("CompositeStrategy should penalize low score"):
    val strategy = CompositeStrategy()
    val client = Client("Marcos", "654.654.654-65", 30, 10000.0, 450, 1000.0, 36)
    val (level, score, reason) = strategy.assess(client)
    assert(score < 100)
    assert(reason.contains("score"))

  test("RiskAssessor should create RiskAssessment"):
    val assessor = RiskAssessor(ScoreBasedStrategy())
    val client = Client("Teresa", "147.147.147-14", 35, 6000.0, 750, 1200.0, 30)
    val assessment = assessor.assess(client)
    assert(assessment.client == client)
    assert(assessment.level == RiskLevel.Medium)

  test("RiskAssessor should process batch assessments"):
    val assessor = RiskAssessor(ScoreBasedStrategy())
    val clients = List(
      Client("Client1", "111.111.111-11", 30, 5000.0, 850, 1000.0, 24),
      Client("Client2", "222.222.222-22", 35, 5000.0, 450, 1000.0, 24)
    )
    val assessments = assessor.assessBatch(clients)
    assert(assessments.length == 2)
    assert(assessments(0).level == RiskLevel.Low)
    assert(assessments(1).level == RiskLevel.VeryHigh)

  test("Client should store all client data"):
    val client = Client("Gabriel", "987.987.987-98", 45, 15000.0, 900, 3000.0, 72)
    assert(client.name == "Gabriel")
    assert(client.cpf == "987.987.987-98")
    assert(client.age == 45)
    assert(client.salary == 15000.0)
    assert(client.creditScore == 900)
    assert(client.debts == 3000.0)
    assert(client.employmentTime == 72)
