package com.rbleggi.creditrisk

enum RiskLevel:
  case Baixo, Medio, Alto, MuitoAlto

case class Cliente(
  nome: String,
  cpf: String,
  idade: Int,
  salario: Double,
  scoreCredito: Int,
  dividas: Double,
  tempoEmprego: Int
)

case class AvaliacaoRisco(cliente: Cliente, nivel: RiskLevel, score: Double, motivo: String)

trait RiskAssessmentStrategy:
  def avaliar(cliente: Cliente): (RiskLevel, Double, String)

class ScoreBasedStrategy extends RiskAssessmentStrategy:
  def avaliar(cliente: Cliente): (RiskLevel, Double, String) =
    val score = cliente.scoreCredito.toDouble

    val (nivel, motivo) = if score >= 800 then
      (RiskLevel.Baixo, "Score de crédito excelente")
    else if score >= 650 then
      (RiskLevel.Medio, "Score de crédito bom")
    else if score >= 500 then
      (RiskLevel.Alto, "Score de crédito regular")
    else
      (RiskLevel.MuitoAlto, "Score de crédito baixo")

    (nivel, score / 10.0, motivo)

class DebtToIncomeStrategy extends RiskAssessmentStrategy:
  def avaliar(cliente: Cliente): (RiskLevel, Double, String) =
    val ratio = if cliente.salario > 0 then
      (cliente.dividas / cliente.salario) * 100
    else
      100.0

    val score = Math.max(0, 100 - ratio)

    val (nivel, motivo) = if ratio <= 20 then
      (RiskLevel.Baixo, f"Dívidas apenas ${ratio}%.1f%% da renda")
    else if ratio <= 40 then
      (RiskLevel.Medio, f"Dívidas ${ratio}%.1f%% da renda")
    else if ratio <= 60 then
      (RiskLevel.Alto, f"Dívidas ${ratio}%.1f%% da renda - comprometimento alto")
    else
      (RiskLevel.MuitoAlto, f"Dívidas ${ratio}%.1f%% da renda - comprometimento crítico")

    (nivel, score, motivo)

class CompositeStrategy extends RiskAssessmentStrategy:
  def avaliar(cliente: Cliente): (RiskLevel, Double, String) =
    var pontos = 100.0
    val motivos = scala.collection.mutable.ListBuffer[String]()

    if cliente.scoreCredito < 500 then
      pontos -= 30
      motivos += "Score baixo"
    else if cliente.scoreCredito < 650 then
      pontos -= 15
      motivos += "Score moderado"

    val debtRatio = if cliente.salario > 0 then
      (cliente.dividas / cliente.salario) * 100
    else
      100.0

    if debtRatio > 60 then
      pontos -= 25
      motivos += "Endividamento alto"
    else if debtRatio > 40 then
      pontos -= 15
      motivos += "Endividamento moderado"

    if cliente.salario < 2000 then
      pontos -= 20
      motivos += "Salário baixo"
    else if cliente.salario < 5000 then
      pontos -= 10

    if cliente.tempoEmprego < 6 then
      pontos -= 15
      motivos += "Pouco tempo de emprego"
    else if cliente.tempoEmprego < 12 then
      pontos -= 5

    if cliente.idade < 21 then
      pontos -= 10
      motivos += "Idade jovem"

    val nivel = if pontos >= 80 then RiskLevel.Baixo
    else if pontos >= 60 then RiskLevel.Medio
    else if pontos >= 40 then RiskLevel.Alto
    else RiskLevel.MuitoAlto

    val motivoFinal = if motivos.isEmpty then "Perfil adequado" else motivos.mkString(", ")

    (nivel, pontos, motivoFinal)

class AvaliadorRisco(strategy: RiskAssessmentStrategy):
  def avaliar(cliente: Cliente): AvaliacaoRisco =
    val (nivel, score, motivo) = strategy.avaliar(cliente)
    AvaliacaoRisco(cliente, nivel, score, motivo)

  def avaliarLote(clientes: List[Cliente]): List[AvaliacaoRisco] =
    clientes.map(avaliar)

@main def run(): Unit =
  val clientes = List(
    Cliente("João Silva", "123.456.789-01", 35, 8000.0, 750, 1500.0, 36),
    Cliente("Maria Santos", "987.654.321-00", 28, 3500.0, 820, 500.0, 24),
    Cliente("Carlos Oliveira", "111.222.333-44", 42, 2500.0, 480, 2000.0, 8),
    Cliente("Ana Costa", "555.666.777-88", 25, 4500.0, 620, 3000.0, 18),
    Cliente("Pedro Souza", "999.888.777-66", 50, 12000.0, 880, 2000.0, 120)
  )

  println("=== Score-Based Strategy ===")
  val avaliadorScore = AvaliadorRisco(ScoreBasedStrategy())
  avaliadorScore.avaliarLote(clientes).foreach { avaliacao =>
    println(f"${avaliacao.cliente.nome}: ${avaliacao.nivel} (Score: ${avaliacao.score}%.1f) - ${avaliacao.motivo}")
  }
  println()

  println("=== Debt-to-Income Strategy ===")
  val avaliadorDebt = AvaliadorRisco(DebtToIncomeStrategy())
  avaliadorDebt.avaliarLote(clientes).foreach { avaliacao =>
    println(f"${avaliacao.cliente.nome}: ${avaliacao.nivel} (Score: ${avaliacao.score}%.1f) - ${avaliacao.motivo}")
  }
  println()

  println("=== Composite Strategy ===")
  val avaliadorComposite = AvaliadorRisco(CompositeStrategy())
  avaliadorComposite.avaliarLote(clientes).foreach { avaliacao =>
    println(f"${avaliacao.cliente.nome}: ${avaliacao.nivel} (Score: ${avaliacao.score}%.1f) - ${avaliacao.motivo}")
  }
