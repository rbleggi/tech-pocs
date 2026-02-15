package com.rbleggi.sentiment

enum Sentiment:
  case Positivo, Negativo, Neutro

case class Avaliacao(texto: String, produto: String)

case class ResultadoAnalise(avaliacao: Avaliacao, sentiment: Sentiment, score: Double)

trait SentimentStrategy:
  def analisar(texto: String): (Sentiment, Double)

class LexiconStrategy extends SentimentStrategy:
  private val palavrasPositivas = Set(
    "bom", "otimo", "excelente", "maravilhoso", "perfeito", "adorei",
    "fantastico", "incrivel", "recomendo", "qualidade", "rapido", "eficiente"
  )

  private val palavrasNegativas = Set(
    "ruim", "pessimo", "horrivel", "terrivel", "defeito", "quebrado",
    "lento", "demorado", "nao recomendo", "decepcionante", "problema"
  )

  def analisar(texto: String): (Sentiment, Double) =
    val textoLimpo = texto.toLowerCase.split("\\s+")
    val positivos = textoLimpo.count(palavrasPositivas.contains)
    val negativos = textoLimpo.count(palavrasNegativas.contains)

    val score = (positivos - negativos).toDouble / textoLimpo.length

    if score > 0.05 then (Sentiment.Positivo, score)
    else if score < -0.05 then (Sentiment.Negativo, score)
    else (Sentiment.Neutro, score)

class RuleBasedStrategy extends SentimentStrategy:
  def analisar(texto: String): (Sentiment, Double) =
    val textoLower = texto.toLowerCase

    val regrasPositivas = List(
      "otimo produto" -> 0.8,
      "muito bom" -> 0.7,
      "recomendo" -> 0.6,
      "adorei" -> 0.8,
      "excelente" -> 0.9
    )

    val regrasNegativas = List(
      "nao recomendo" -> -0.8,
      "pessimo" -> -0.9,
      "nao funciona" -> -0.7,
      "defeito" -> -0.6
    )

    var score = 0.0

    regrasPositivas.foreach { (regra, peso) =>
      if textoLower.contains(regra) then score += peso
    }

    regrasNegativas.foreach { (regra, peso) =>
      if textoLower.contains(regra) then score += peso
    }

    if score > 0.3 then (Sentiment.Positivo, score)
    else if score < -0.3 then (Sentiment.Negativo, score)
    else (Sentiment.Neutro, score)

class HybridStrategy extends SentimentStrategy:
  private val lexicon = LexiconStrategy()
  private val ruleBased = RuleBasedStrategy()

  def analisar(texto: String): (Sentiment, Double) =
    val (sent1, score1) = lexicon.analisar(texto)
    val (sent2, score2) = ruleBased.analisar(texto)

    val scoreCombi = (score1 + score2) / 2

    if scoreCombi > 0.2 then (Sentiment.Positivo, scoreCombi)
    else if scoreCombi < -0.2 then (Sentiment.Negativo, scoreCombi)
    else (Sentiment.Neutro, scoreCombi)

class AnalisadorSentimento(strategy: SentimentStrategy):
  def analisar(avaliacao: Avaliacao): ResultadoAnalise =
    val (sentiment, score) = strategy.analisar(avaliacao.texto)
    ResultadoAnalise(avaliacao, sentiment, score)

  def analisarLote(avaliacoes: List[Avaliacao]): List[ResultadoAnalise] =
    avaliacoes.map(analisar)

@main def run(): Unit =
  val avaliacoes = List(
    Avaliacao("Produto excelente, muito bom mesmo. Recomendo!", "Notebook Dell"),
    Avaliacao("Pessimo, nao funciona direito. Nao recomendo.", "Mouse Wireless"),
    Avaliacao("Produto ok, nada de especial", "Teclado"),
    Avaliacao("Adorei! Otimo produto, entrega rapida", "Monitor LG"),
    Avaliacao("Veio com defeito, muito ruim", "Webcam")
  )

  println("=== Lexicon Strategy ===")
  val analisadorLexicon = AnalisadorSentimento(LexiconStrategy())
  analisadorLexicon.analisarLote(avaliacoes).foreach { resultado =>
    println(f"${resultado.avaliacao.produto}: ${resultado.sentiment} (${resultado.score}%.3f)")
  }
  println()

  println("=== Rule-Based Strategy ===")
  val analisadorRuleBased = AnalisadorSentimento(RuleBasedStrategy())
  analisadorRuleBased.analisarLote(avaliacoes).foreach { resultado =>
    println(f"${resultado.avaliacao.produto}: ${resultado.sentiment} (${resultado.score}%.3f)")
  }
  println()

  println("=== Hybrid Strategy ===")
  val analisadorHybrid = AnalisadorSentimento(HybridStrategy())
  analisadorHybrid.analisarLote(avaliacoes).foreach { resultado =>
    println(f"${resultado.avaliacao.produto}: ${resultado.sentiment} (${resultado.score}%.3f)")
  }
