package com.rbleggi.anomaly

case class Transacao(id: String, valor: Double, timestamp: Long, cpf: String)

case class ResultadoDeteccao(transacao: Transacao, anomalia: Boolean, score: Double, motivo: String)

trait AnomalyDetectionStrategy:
  def detectar(transacoes: List[Transacao]): List[ResultadoDeteccao]

class ZScoreStrategy extends AnomalyDetectionStrategy:
  private val limiar = 1.5

  private def calcularMedia(valores: List[Double]): Double =
    valores.sum / valores.length

  private def calcularDesvioPadrao(valores: List[Double], media: Double): Double =
    val variancia = valores.map(v => Math.pow(v - media, 2)).sum / valores.length
    Math.sqrt(variancia)

  def detectar(transacoes: List[Transacao]): List[ResultadoDeteccao] =
    val valores = transacoes.map(_.valor)
    val media = calcularMedia(valores)
    val desvioPadrao = calcularDesvioPadrao(valores, media)

    transacoes.map { transacao =>
      val zScore = Math.abs((transacao.valor - media) / desvioPadrao)
      val anomalia = zScore > limiar
      val motivo = if anomalia then f"Z-Score ${zScore}%.2f excede limiar $limiar" else "Normal"
      ResultadoDeteccao(transacao, anomalia, zScore, motivo)
    }

class IQRStrategy extends AnomalyDetectionStrategy:
  private def calcularQuartis(valores: List[Double]): (Double, Double, Double) =
    val sorted = valores.sorted
    val n = sorted.length
    val q1 = sorted(n / 4)
    val q2 = sorted(n / 2)
    val q3 = sorted(3 * n / 4)
    (q1, q2, q3)

  def detectar(transacoes: List[Transacao]): List[ResultadoDeteccao] =
    val valores = transacoes.map(_.valor)
    val (q1, q2, q3) = calcularQuartis(valores)
    val iqr = q3 - q1
    val limiteInferior = q1 - 1.5 * iqr
    val limiteSuperior = q3 + 1.5 * iqr

    transacoes.map { transacao =>
      val anomalia = transacao.valor < limiteInferior || transacao.valor > limiteSuperior
      val distancia = if transacao.valor < limiteInferior then
        limiteInferior - transacao.valor
      else if transacao.valor > limiteSuperior then
        transacao.valor - limiteSuperior
      else
        0.0

      val score = distancia / iqr
      val motivo = if anomalia then
        f"Valor fora do intervalo IQR [${limiteInferior}%.2f, ${limiteSuperior}%.2f]"
      else
        "Normal"

      ResultadoDeteccao(transacao, anomalia, score, motivo)
    }

class IsolationStrategy extends AnomalyDetectionStrategy:
  private val limiarAlto = 10000.0
  private val limiarMuitoAlto = 50000.0

  def detectar(transacoes: List[Transacao]): List[ResultadoDeteccao] =
    val agrupadoPorCpf = transacoes.groupBy(_.cpf)

    transacoes.map { transacao =>
      val transacoesCpf = agrupadoPorCpf(transacao.cpf)
      val mediaUsuario = transacoesCpf.map(_.valor).sum / transacoesCpf.length

      val score = transacao.valor / mediaUsuario
      val anomalia = transacao.valor > limiarAlto || score > 5.0

      val motivo = if transacao.valor > limiarMuitoAlto then
        f"Valor muito alto: R$$ ${transacao.valor}%,.2f"
      else if transacao.valor > limiarAlto then
        f"Valor alto: R$$ ${transacao.valor}%,.2f"
      else if score > 5.0 then
        f"Valor ${score}%.1fx maior que média do usuário"
      else
        "Normal"

      ResultadoDeteccao(transacao, anomalia, score, motivo)
    }

class DetectorAnomalias(strategy: AnomalyDetectionStrategy):
  def detectar(transacoes: List[Transacao]): List[ResultadoDeteccao] =
    strategy.detectar(transacoes)

  def detectarAnomalias(transacoes: List[Transacao]): List[ResultadoDeteccao] =
    detectar(transacoes).filter(_.anomalia)

@main def run(): Unit =
  val transacoes = List(
    Transacao("T001", 150.00, 1000L, "123.456.789-01"),
    Transacao("T002", 200.00, 2000L, "123.456.789-01"),
    Transacao("T003", 15000.00, 3000L, "123.456.789-01"),
    Transacao("T004", 120.00, 4000L, "987.654.321-00"),
    Transacao("T005", 180.00, 5000L, "987.654.321-00"),
    Transacao("T006", 160.00, 6000L, "111.222.333-44"),
    Transacao("T007", 75000.00, 7000L, "111.222.333-44"),
    Transacao("T008", 95.00, 8000L, "111.222.333-44")
  )

  println("=== Z-Score Strategy ===")
  val detectorZScore = DetectorAnomalias(ZScoreStrategy())
  detectorZScore.detectarAnomalias(transacoes).foreach { resultado =>
    println(f"${resultado.transacao.id}: R$$ ${resultado.transacao.valor}%,.2f - ${resultado.motivo}")
  }
  println()

  println("=== IQR Strategy ===")
  val detectorIQR = DetectorAnomalias(IQRStrategy())
  detectorIQR.detectarAnomalias(transacoes).foreach { resultado =>
    println(f"${resultado.transacao.id}: R$$ ${resultado.transacao.valor}%,.2f - ${resultado.motivo}")
  }
  println()

  println("=== Isolation Strategy ===")
  val detectorIsolation = DetectorAnomalias(IsolationStrategy())
  detectorIsolation.detectarAnomalias(transacoes).foreach { resultado =>
    println(f"${resultado.transacao.id}: R$$ ${resultado.transacao.valor}%,.2f - ${resultado.motivo}")
  }
