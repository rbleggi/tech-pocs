package com.rbleggi.anomaly

import org.scalatest.funsuite.AnyFunSuite

class AnomalyDetectionSpec extends AnyFunSuite:

  test("ZScoreStrategy should detect anomaly for high value"):
    val transactions = List(
      Transaction("T1", 100.0, 1000L, "123.456.789-01"),
      Transaction("T2", 110.0, 2000L, "123.456.789-01"),
      Transaction("T3", 105.0, 3000L, "123.456.789-01"),
      Transaction("T4", 5000.0, 4000L, "123.456.789-01")
    )
    val strategy = ZScoreStrategy()
    val results = strategy.detect(transactions)
    val anomalies = results.filter(_.anomaly)
    assert(anomalies.nonEmpty)
    assert(anomalies.exists(_.transaction.id == "T4"))

  test("ZScoreStrategy should not flag normal values as anomalies"):
    val transactions = List(
      Transaction("T1", 100.0, 1000L, "123.456.789-01"),
      Transaction("T2", 110.0, 2000L, "123.456.789-01"),
      Transaction("T3", 105.0, 3000L, "123.456.789-01"),
      Transaction("T4", 115.0, 4000L, "123.456.789-01")
    )
    val strategy = ZScoreStrategy()
    val results = strategy.detect(transactions)
    val normal = results.filterNot(_.anomaly)
    assert(normal.length == 4)

  test("IQRStrategy should detect outliers"):
    val transactions = List(
      Transaction("T1", 100.0, 1000L, "123.456.789-01"),
      Transaction("T2", 120.0, 2000L, "123.456.789-01"),
      Transaction("T3", 110.0, 3000L, "123.456.789-01"),
      Transaction("T4", 130.0, 4000L, "123.456.789-01"),
      Transaction("T5", 10000.0, 5000L, "123.456.789-01")
    )
    val strategy = IQRStrategy()
    val results = strategy.detect(transactions)
    val anomalies = results.filter(_.anomaly)
    assert(anomalies.nonEmpty)

  test("IQRStrategy should handle uniform data"):
    val transactions = List(
      Transaction("T1", 100.0, 1000L, "123.456.789-01"),
      Transaction("T2", 100.0, 2000L, "123.456.789-01"),
      Transaction("T3", 100.0, 3000L, "123.456.789-01")
    )
    val strategy = IQRStrategy()
    val results = strategy.detect(transactions)
    val anomalies = results.filter(_.anomaly)
    assert(anomalies.isEmpty)

  test("IsolationStrategy should detect high value transactions"):
    val transactions = List(
      Transaction("T1", 150.0, 1000L, "123.456.789-01"),
      Transaction("T2", 15000.0, 2000L, "123.456.789-01")
    )
    val strategy = IsolationStrategy()
    val results = strategy.detect(transactions)
    val anomalies = results.filter(_.anomaly)
    assert(anomalies.nonEmpty)
    assert(anomalies.exists(_.transaction.id == "T2"))

  test("IsolationStrategy should detect transactions much higher than user average"):
    val transactions = List(
      Transaction("T1", 100.0, 1000L, "123.456.789-01"),
      Transaction("T2", 110.0, 2000L, "123.456.789-01"),
      Transaction("T3", 1000.0, 3000L, "123.456.789-01")
    )
    val strategy = IsolationStrategy()
    val results = strategy.detect(transactions)
    val result3 = results.find(_.transaction.id == "T3").get
    assert(result3.score > 1.0)

  test("AnomalyDetector should use strategy"):
    val transactions = List(
      Transaction("T1", 100.0, 1000L, "123.456.789-01"),
      Transaction("T2", 5000.0, 2000L, "123.456.789-01")
    )
    val detector = AnomalyDetector(ZScoreStrategy())
    val results = detector.detect(transactions)
    assert(results.length == 2)

  test("AnomalyDetector should filter only anomalies"):
    val transactions = List(
      Transaction("T1", 100.0, 1000L, "123.456.789-01"),
      Transaction("T2", 110.0, 2000L, "123.456.789-01"),
      Transaction("T3", 20000.0, 3000L, "123.456.789-01")
    )
    val detector = AnomalyDetector(IsolationStrategy())
    val anomalies = detector.detectAnomalies(transactions)
    assert(anomalies.forall(_.anomaly))

  test("Transaction should store transaction data"):
    val transaction = Transaction("T123", 999.99, 12345L, "987.654.321-00")
    assert(transaction.id == "T123")
    assert(transaction.amount == 999.99)
    assert(transaction.timestamp == 12345L)
    assert(transaction.cpf == "987.654.321-00")

  test("DetectionResult should store detection result"):
    val transaction = Transaction("T1", 100.0, 1000L, "123.456.789-01")
    val result = DetectionResult(transaction, true, 3.5, "Anomaly detected")
    assert(result.anomaly)
    assert(result.score == 3.5)
    assert(result.reason == "Anomaly detected")
