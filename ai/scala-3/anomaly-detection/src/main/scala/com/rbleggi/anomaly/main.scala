package com.rbleggi.anomaly

case class Transaction(id: String, amount: Double, timestamp: Long, cpf: String)

case class DetectionResult(transaction: Transaction, anomaly: Boolean, score: Double, reason: String)

trait AnomalyDetectionStrategy:
  def detect(transactions: List[Transaction]): List[DetectionResult]

class ZScoreStrategy extends AnomalyDetectionStrategy:
  private val threshold = 1.5

  private def calculateMean(values: List[Double]): Double =
    values.sum / values.length

  private def calculateStdDev(values: List[Double], mean: Double): Double =
    val variance = values.map(v => Math.pow(v - mean, 2)).sum / values.length
    Math.sqrt(variance)

  def detect(transactions: List[Transaction]): List[DetectionResult] =
    val values = transactions.map(_.amount)
    val mean = calculateMean(values)
    val stdDev = calculateStdDev(values, mean)

    transactions.map { transaction =>
      val zScore = Math.abs((transaction.amount - mean) / stdDev)
      val anomaly = zScore > threshold
      val reason = if anomaly then f"Z-Score ${zScore}%.2f exceeds threshold $threshold" else "Normal"
      DetectionResult(transaction, anomaly, zScore, reason)
    }

class IQRStrategy extends AnomalyDetectionStrategy:
  private def calculateQuartiles(values: List[Double]): (Double, Double, Double) =
    val sorted = values.sorted
    val n = sorted.length
    val q1 = sorted(n / 4)
    val q2 = sorted(n / 2)
    val q3 = sorted(3 * n / 4)
    (q1, q2, q3)

  def detect(transactions: List[Transaction]): List[DetectionResult] =
    val values = transactions.map(_.amount)
    val (q1, q2, q3) = calculateQuartiles(values)
    val iqr = q3 - q1
    val lowerLimit = q1 - 1.5 * iqr
    val upperLimit = q3 + 1.5 * iqr

    transactions.map { transaction =>
      val anomaly = transaction.amount < lowerLimit || transaction.amount > upperLimit
      val distance = if transaction.amount < lowerLimit then
        lowerLimit - transaction.amount
      else if transaction.amount > upperLimit then
        transaction.amount - upperLimit
      else
        0.0

      val score = distance / iqr
      val reason = if anomaly then
        f"Amount outside IQR range [${lowerLimit}%.2f, ${upperLimit}%.2f]"
      else
        "Normal"

      DetectionResult(transaction, anomaly, score, reason)
    }

class IsolationStrategy extends AnomalyDetectionStrategy:
  private val highThreshold = 10000.0
  private val veryHighThreshold = 50000.0

  def detect(transactions: List[Transaction]): List[DetectionResult] =
    val groupedByCpf = transactions.groupBy(_.cpf)

    transactions.map { transaction =>
      val userTransactions = groupedByCpf(transaction.cpf)
      val userMean = userTransactions.map(_.amount).sum / userTransactions.length

      val score = transaction.amount / userMean
      val anomaly = transaction.amount > highThreshold || score > 5.0

      val reason = if transaction.amount > veryHighThreshold then
        f"Very high amount: R$$ ${transaction.amount}%,.2f"
      else if transaction.amount > highThreshold then
        f"High amount: R$$ ${transaction.amount}%,.2f"
      else if score > 5.0 then
        f"Amount ${score}%.1fx higher than user average"
      else
        "Normal"

      DetectionResult(transaction, anomaly, score, reason)
    }

class AnomalyDetector(strategy: AnomalyDetectionStrategy):
  def detect(transactions: List[Transaction]): List[DetectionResult] =
    strategy.detect(transactions)

  def detectAnomalies(transactions: List[Transaction]): List[DetectionResult] =
    detect(transactions).filter(_.anomaly)

@main def run(): Unit =
  println("Anomaly Detection")
