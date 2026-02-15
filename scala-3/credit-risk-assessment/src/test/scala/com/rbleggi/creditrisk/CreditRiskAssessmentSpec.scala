package com.rbleggi.creditrisk

import org.scalatest.funsuite.AnyFunSuite

class CreditRiskAssessmentSpec extends AnyFunSuite:

  test("ScoreBasedStrategy should classify high score as low risk"):
    val strategy = ScoreBasedStrategy()
    val cliente = Cliente("João", "123.456.789-01", 30, 5000.0, 850, 1000.0, 24)
    val (nivel, _, _) = strategy.avaliar(cliente)
    assert(nivel == RiskLevel.Baixo)

  test("ScoreBasedStrategy should classify good score as medium risk"):
    val strategy = ScoreBasedStrategy()
    val cliente = Cliente("Maria", "987.654.321-00", 28, 5000.0, 700, 1000.0, 24)
    val (nivel, _, _) = strategy.avaliar(cliente)
    assert(nivel == RiskLevel.Medio)

  test("ScoreBasedStrategy should classify regular score as high risk"):
    val strategy = ScoreBasedStrategy()
    val cliente = Cliente("Carlos", "111.222.333-44", 35, 5000.0, 550, 1000.0, 24)
    val (nivel, _, _) = strategy.avaliar(cliente)
    assert(nivel == RiskLevel.Alto)

  test("ScoreBasedStrategy should classify low score as very high risk"):
    val strategy = ScoreBasedStrategy()
    val cliente = Cliente("Ana", "555.666.777-88", 32, 5000.0, 400, 1000.0, 24)
    val (nivel, _, _) = strategy.avaliar(cliente)
    assert(nivel == RiskLevel.MuitoAlto)

  test("DebtToIncomeStrategy should classify low debt as low risk"):
    val strategy = DebtToIncomeStrategy()
    val cliente = Cliente("Pedro", "999.888.777-66", 40, 10000.0, 700, 1500.0, 36)
    val (nivel, _, _) = strategy.avaliar(cliente)
    assert(nivel == RiskLevel.Baixo)

  test("DebtToIncomeStrategy should classify medium debt as medium risk"):
    val strategy = DebtToIncomeStrategy()
    val cliente = Cliente("Lucia", "123.123.123-12", 35, 5000.0, 700, 1800.0, 24)
    val (nivel, _, _) = strategy.avaliar(cliente)
    assert(nivel == RiskLevel.Medio)

  test("DebtToIncomeStrategy should classify high debt as high risk"):
    val strategy = DebtToIncomeStrategy()
    val cliente = Cliente("Roberto", "456.456.456-45", 30, 3000.0, 700, 1800.0, 24)
    val (nivel, _, _) = strategy.avaliar(cliente)
    assert(nivel == RiskLevel.Alto)

  test("DebtToIncomeStrategy should classify very high debt as very high risk"):
    val strategy = DebtToIncomeStrategy()
    val cliente = Cliente("Fernanda", "789.789.789-78", 28, 2000.0, 700, 1500.0, 24)
    val (nivel, _, _) = strategy.avaliar(cliente)
    assert(nivel == RiskLevel.MuitoAlto)

  test("CompositeStrategy should combine multiple factors"):
    val strategy = CompositeStrategy()
    val cliente = Cliente("Joana", "321.321.321-32", 40, 10000.0, 850, 1000.0, 60)
    val (nivel, score, _) = strategy.avaliar(cliente)
    assert(nivel == RiskLevel.Baixo)
    assert(score >= 80)

  test("CompositeStrategy should penalize low score"):
    val strategy = CompositeStrategy()
    val cliente = Cliente("Marcos", "654.654.654-65", 30, 10000.0, 450, 1000.0, 36)
    val (nivel, score, motivo) = strategy.avaliar(cliente)
    assert(score < 100)
    assert(motivo.contains("Score"))

  test("AvaliadorRisco should create AvaliacaoRisco"):
    val avaliador = AvaliadorRisco(ScoreBasedStrategy())
    val cliente = Cliente("Teresa", "147.147.147-14", 35, 6000.0, 750, 1200.0, 30)
    val avaliacao = avaliador.avaliar(cliente)
    assert(avaliacao.cliente == cliente)
    assert(avaliacao.nivel == RiskLevel.Medio)

  test("AvaliadorRisco should process batch assessments"):
    val avaliador = AvaliadorRisco(ScoreBasedStrategy())
    val clientes = List(
      Cliente("Cliente1", "111.111.111-11", 30, 5000.0, 850, 1000.0, 24),
      Cliente("Cliente2", "222.222.222-22", 35, 5000.0, 450, 1000.0, 24)
    )
    val avaliacoes = avaliador.avaliarLote(clientes)
    assert(avaliacoes.length == 2)
    assert(avaliacoes(0).nivel == RiskLevel.Baixo)
    assert(avaliacoes(1).nivel == RiskLevel.MuitoAlto)

  test("Cliente should store all client data"):
    val cliente = Cliente("Gabriel", "987.987.987-98", 45, 15000.0, 900, 3000.0, 72)
    assert(cliente.nome == "Gabriel")
    assert(cliente.cpf == "987.987.987-98")
    assert(cliente.idade == 45)
    assert(cliente.salario == 15000.0)
    assert(cliente.scoreCredito == 900)
    assert(cliente.dividas == 3000.0)
    assert(cliente.tempoEmprego == 72)
