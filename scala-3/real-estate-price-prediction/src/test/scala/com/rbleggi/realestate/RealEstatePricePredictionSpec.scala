package com.rbleggi.realestate

import org.scalatest.funsuite.AnyFunSuite

class RealEstatePricePredictionSpec extends AnyFunSuite:

  test("LinearRegressionStrategy should predict higher prices for larger area"):
    val strategy = LinearRegressionStrategy()
    val imovel1 = Imovel(50, 2, "Centro", "Curitiba", 5)
    val imovel2 = Imovel(150, 2, "Centro", "Curitiba", 5)
    val (preco1, _) = strategy.prever(imovel1)
    val (preco2, _) = strategy.prever(imovel2)
    assert(preco2 > preco1)

  test("LinearRegressionStrategy should predict higher prices for more rooms"):
    val strategy = LinearRegressionStrategy()
    val imovel1 = Imovel(100, 1, "Centro", "Curitiba", 5)
    val imovel2 = Imovel(100, 4, "Centro", "Curitiba", 5)
    val (preco1, _) = strategy.prever(imovel1)
    val (preco2, _) = strategy.prever(imovel2)
    assert(preco2 > preco1)

  test("LinearRegressionStrategy should predict lower prices for older properties"):
    val strategy = LinearRegressionStrategy()
    val imovel1 = Imovel(100, 2, "Centro", "Curitiba", 1)
    val imovel2 = Imovel(100, 2, "Centro", "Curitiba", 20)
    val (preco1, _) = strategy.prever(imovel1)
    val (preco2, _) = strategy.prever(imovel2)
    assert(preco1 > preco2)

  test("LinearRegressionStrategy should apply neighborhood multiplier"):
    val strategy = LinearRegressionStrategy()
    val imovel1 = Imovel(100, 2, "Centro", "Curitiba", 5)
    val imovel2 = Imovel(100, 2, "Jardins", "Curitiba", 5)
    val (preco1, _) = strategy.prever(imovel1)
    val (preco2, _) = strategy.prever(imovel2)
    assert(preco2 > preco1)

  test("LinearRegressionStrategy should apply city multiplier"):
    val strategy = LinearRegressionStrategy()
    val imovel1 = Imovel(100, 2, "Centro", "Belo Horizonte", 5)
    val imovel2 = Imovel(100, 2, "Centro", "Sao Paulo", 5)
    val (preco1, _) = strategy.prever(imovel1)
    val (preco2, _) = strategy.prever(imovel2)
    assert(preco2 > preco1)

  test("KNNStrategy should return positive price"):
    val strategy = KNNStrategy()
    val imovel = Imovel(90, 3, "Centro", "Curitiba", 8)
    val (preco, confianca) = strategy.prever(imovel)
    assert(preco > 0)
    assert(confianca > 0 && confianca <= 1.0)

  test("KNNStrategy should have consistent confidence"):
    val strategy = KNNStrategy()
    val imovel = Imovel(100, 3, "Jardins", "Sao Paulo", 5)
    val (_, confianca) = strategy.prever(imovel)
    assert(confianca == 0.82)

  test("EnsembleStrategy should combine predictions"):
    val strategy = EnsembleStrategy()
    val imovel = Imovel(100, 3, "Centro", "Curitiba", 5)
    val (preco, confianca) = strategy.prever(imovel)
    assert(preco > 0)
    assert(confianca > 0 && confianca <= 1.0)

  test("PrevisaoPreco should create Predicao with correct data"):
    val previsao = PrevisaoPreco(LinearRegressionStrategy())
    val imovel = Imovel(100, 3, "Centro", "Curitiba", 5)
    val resultado = previsao.prever(imovel)
    assert(resultado.imovel == imovel)
    assert(resultado.precoEstimado > 0)
    assert(resultado.confianca > 0)

  test("PrevisaoPreco should process batch predictions"):
    val previsao = PrevisaoPreco(LinearRegressionStrategy())
    val imoveis = List(
      Imovel(100, 3, "Centro", "Curitiba", 5),
      Imovel(80, 2, "Jardins", "Sao Paulo", 3)
    )
    val resultados = previsao.preverLote(imoveis)
    assert(resultados.length == 2)
    assert(resultados.forall(_.precoEstimado > 0))

  test("Imovel should store all properties"):
    val imovel = Imovel(120, 4, "Batel", "Curitiba", 2)
    assert(imovel.area == 120)
    assert(imovel.quartos == 4)
    assert(imovel.bairro == "Batel")
    assert(imovel.cidade == "Curitiba")
    assert(imovel.idade == 2)
