package com.rbleggi.anomaly

import org.scalatest.funsuite.AnyFunSuite

class AnomalyDetectionSpec extends AnyFunSuite:

  test("ZScoreStrategy should detect anomaly for high value"):
    val transacoes = List(
      Transacao("T1", 100.0, 1000L, "123.456.789-01"),
      Transacao("T2", 110.0, 2000L, "123.456.789-01"),
      Transacao("T3", 105.0, 3000L, "123.456.789-01"),
      Transacao("T4", 5000.0, 4000L, "123.456.789-01")
    )
    val strategy = ZScoreStrategy()
    val resultados = strategy.detectar(transacoes)
    val anomalias = resultados.filter(_.anomalia)
    assert(anomalias.nonEmpty)
    assert(anomalias.exists(_.transacao.id == "T4"))

  test("ZScoreStrategy should not flag normal values as anomalies"):
    val transacoes = List(
      Transacao("T1", 100.0, 1000L, "123.456.789-01"),
      Transacao("T2", 110.0, 2000L, "123.456.789-01"),
      Transacao("T3", 105.0, 3000L, "123.456.789-01"),
      Transacao("T4", 115.0, 4000L, "123.456.789-01")
    )
    val strategy = ZScoreStrategy()
    val resultados = strategy.detectar(transacoes)
    val normais = resultados.filterNot(_.anomalia)
    assert(normais.length == 4)

  test("IQRStrategy should detect outliers"):
    val transacoes = List(
      Transacao("T1", 100.0, 1000L, "123.456.789-01"),
      Transacao("T2", 120.0, 2000L, "123.456.789-01"),
      Transacao("T3", 110.0, 3000L, "123.456.789-01"),
      Transacao("T4", 130.0, 4000L, "123.456.789-01"),
      Transacao("T5", 10000.0, 5000L, "123.456.789-01")
    )
    val strategy = IQRStrategy()
    val resultados = strategy.detectar(transacoes)
    val anomalias = resultados.filter(_.anomalia)
    assert(anomalias.nonEmpty)

  test("IQRStrategy should handle uniform data"):
    val transacoes = List(
      Transacao("T1", 100.0, 1000L, "123.456.789-01"),
      Transacao("T2", 100.0, 2000L, "123.456.789-01"),
      Transacao("T3", 100.0, 3000L, "123.456.789-01")
    )
    val strategy = IQRStrategy()
    val resultados = strategy.detectar(transacoes)
    val anomalias = resultados.filter(_.anomalia)
    assert(anomalias.isEmpty)

  test("IsolationStrategy should detect high value transactions"):
    val transacoes = List(
      Transacao("T1", 150.0, 1000L, "123.456.789-01"),
      Transacao("T2", 15000.0, 2000L, "123.456.789-01")
    )
    val strategy = IsolationStrategy()
    val resultados = strategy.detectar(transacoes)
    val anomalias = resultados.filter(_.anomalia)
    assert(anomalias.nonEmpty)
    assert(anomalias.exists(_.transacao.id == "T2"))

  test("IsolationStrategy should detect transactions much higher than user average"):
    val transacoes = List(
      Transacao("T1", 100.0, 1000L, "123.456.789-01"),
      Transacao("T2", 110.0, 2000L, "123.456.789-01"),
      Transacao("T3", 1000.0, 3000L, "123.456.789-01")
    )
    val strategy = IsolationStrategy()
    val resultados = strategy.detectar(transacoes)
    val resultado3 = resultados.find(_.transacao.id == "T3").get
    assert(resultado3.score > 1.0)

  test("DetectorAnomalias should use strategy"):
    val transacoes = List(
      Transacao("T1", 100.0, 1000L, "123.456.789-01"),
      Transacao("T2", 5000.0, 2000L, "123.456.789-01")
    )
    val detector = DetectorAnomalias(ZScoreStrategy())
    val resultados = detector.detectar(transacoes)
    assert(resultados.length == 2)

  test("DetectorAnomalias should filter only anomalies"):
    val transacoes = List(
      Transacao("T1", 100.0, 1000L, "123.456.789-01"),
      Transacao("T2", 110.0, 2000L, "123.456.789-01"),
      Transacao("T3", 20000.0, 3000L, "123.456.789-01")
    )
    val detector = DetectorAnomalias(IsolationStrategy())
    val anomalias = detector.detectarAnomalias(transacoes)
    assert(anomalias.forall(_.anomalia))

  test("Transacao should store transaction data"):
    val transacao = Transacao("T123", 999.99, 12345L, "987.654.321-00")
    assert(transacao.id == "T123")
    assert(transacao.valor == 999.99)
    assert(transacao.timestamp == 12345L)
    assert(transacao.cpf == "987.654.321-00")

  test("ResultadoDeteccao should store detection result"):
    val transacao = Transacao("T1", 100.0, 1000L, "123.456.789-01")
    val resultado = ResultadoDeteccao(transacao, true, 3.5, "Anomalia detectada")
    assert(resultado.anomalia)
    assert(resultado.score == 3.5)
    assert(resultado.motivo == "Anomalia detectada")
