package com.rbleggi.sentiment

import org.scalatest.funsuite.AnyFunSuite

class SentimentAnalysisSpec extends AnyFunSuite:

  test("LexiconStrategy should identify positive sentiment"):
    val strategy = LexiconStrategy()
    val (sentiment, score) = strategy.analisar("Produto excelente e otimo")
    assert(sentiment == Sentiment.Positivo)
    assert(score > 0)

  test("LexiconStrategy should identify negative sentiment"):
    val strategy = LexiconStrategy()
    val (sentiment, score) = strategy.analisar("Produto pessimo e ruim")
    assert(sentiment == Sentiment.Negativo)
    assert(score < 0)

  test("LexiconStrategy should identify neutral sentiment"):
    val strategy = LexiconStrategy()
    val (sentiment, score) = strategy.analisar("O produto chegou hoje")
    assert(sentiment == Sentiment.Neutro)

  test("RuleBasedStrategy should identify positive with strong rule"):
    val strategy = RuleBasedStrategy()
    val (sentiment, score) = strategy.analisar("Otimo produto, muito bom")
    assert(sentiment == Sentiment.Positivo)
    assert(score > 0.3)

  test("RuleBasedStrategy should identify negative with strong rule"):
    val strategy = RuleBasedStrategy()
    val (sentiment, score) = strategy.analisar("Nao recomendo, produto pessimo")
    assert(sentiment == Sentiment.Negativo)
    assert(score < -0.3)

  test("RuleBasedStrategy should identify neutral without matching rules"):
    val strategy = RuleBasedStrategy()
    val (sentiment, score) = strategy.analisar("Produto normal")
    assert(sentiment == Sentiment.Neutro)

  test("HybridStrategy should combine both approaches"):
    val strategy = HybridStrategy()
    val (sentiment, score) = strategy.analisar("Excelente produto, recomendo")
    assert(sentiment == Sentiment.Positivo)

  test("AnalisadorSentimento should analyze single review"):
    val analisador = AnalisadorSentimento(LexiconStrategy())
    val avaliacao = Avaliacao("Produto maravilhoso", "Notebook")
    val resultado = analisador.analisar(avaliacao)
    assert(resultado.sentiment == Sentiment.Positivo)
    assert(resultado.avaliacao.produto == "Notebook")

  test("AnalisadorSentimento should analyze batch of reviews"):
    val analisador = AnalisadorSentimento(LexiconStrategy())
    val avaliacoes = List(
      Avaliacao("Otimo produto", "Mouse"),
      Avaliacao("Produto ruim", "Teclado")
    )
    val resultados = analisador.analisarLote(avaliacoes)
    assert(resultados.length == 2)
    assert(resultados(0).sentiment == Sentiment.Positivo)
    assert(resultados(1).sentiment == Sentiment.Negativo)

  test("Avaliacao should store text and product"):
    val avaliacao = Avaliacao("Bom produto", "Monitor")
    assert(avaliacao.texto == "Bom produto")
    assert(avaliacao.produto == "Monitor")

  test("ResultadoAnalise should contain all analysis data"):
    val avaliacao = Avaliacao("Teste", "Produto")
    val resultado = ResultadoAnalise(avaliacao, Sentiment.Positivo, 0.5)
    assert(resultado.sentiment == Sentiment.Positivo)
    assert(resultado.score == 0.5)
    assert(resultado.avaliacao.texto == "Teste")
