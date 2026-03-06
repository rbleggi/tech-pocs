package com.rbleggi.realestate

import org.scalatest.funsuite.AnyFunSuite

class RealEstatePricePredictionSpec extends AnyFunSuite:

  test("LinearRegressionStrategy should predict higher prices for larger area"):
    val strategy = LinearRegressionStrategy()
    val property1 = Property(50, 2, "Centro", "Curitiba", 5)
    val property2 = Property(150, 2, "Centro", "Curitiba", 5)
    val (price1, _) = strategy.predict(property1)
    val (price2, _) = strategy.predict(property2)
    assert(price2 > price1)

  test("LinearRegressionStrategy should predict higher prices for more rooms"):
    val strategy = LinearRegressionStrategy()
    val property1 = Property(100, 1, "Centro", "Curitiba", 5)
    val property2 = Property(100, 4, "Centro", "Curitiba", 5)
    val (price1, _) = strategy.predict(property1)
    val (price2, _) = strategy.predict(property2)
    assert(price2 > price1)

  test("LinearRegressionStrategy should predict lower prices for older properties"):
    val strategy = LinearRegressionStrategy()
    val property1 = Property(100, 2, "Centro", "Curitiba", 1)
    val property2 = Property(100, 2, "Centro", "Curitiba", 20)
    val (price1, _) = strategy.predict(property1)
    val (price2, _) = strategy.predict(property2)
    assert(price1 > price2)

  test("LinearRegressionStrategy should apply neighborhood multiplier"):
    val strategy = LinearRegressionStrategy()
    val property1 = Property(100, 2, "Centro", "Curitiba", 5)
    val property2 = Property(100, 2, "Jardins", "Curitiba", 5)
    val (price1, _) = strategy.predict(property1)
    val (price2, _) = strategy.predict(property2)
    assert(price2 > price1)

  test("LinearRegressionStrategy should apply city multiplier"):
    val strategy = LinearRegressionStrategy()
    val property1 = Property(100, 2, "Centro", "Belo Horizonte", 5)
    val property2 = Property(100, 2, "Centro", "Sao Paulo", 5)
    val (price1, _) = strategy.predict(property1)
    val (price2, _) = strategy.predict(property2)
    assert(price2 > price1)

  test("KNNStrategy should return positive price"):
    val strategy = KNNStrategy()
    val property = Property(90, 3, "Centro", "Curitiba", 8)
    val (price, confidence) = strategy.predict(property)
    assert(price > 0)
    assert(confidence > 0 && confidence <= 1.0)

  test("KNNStrategy should have consistent confidence"):
    val strategy = KNNStrategy()
    val property = Property(100, 3, "Jardins", "Sao Paulo", 5)
    val (_, confidence) = strategy.predict(property)
    assert(confidence == 0.82)

  test("EnsembleStrategy should combine predictions"):
    val strategy = EnsembleStrategy()
    val property = Property(100, 3, "Centro", "Curitiba", 5)
    val (price, confidence) = strategy.predict(property)
    assert(price > 0)
    assert(confidence > 0 && confidence <= 1.0)

  test("PricePredictor should create Prediction with correct data"):
    val predictor = PricePredictor(LinearRegressionStrategy())
    val property = Property(100, 3, "Centro", "Curitiba", 5)
    val result = predictor.predict(property)
    assert(result.property == property)
    assert(result.estimatedPrice > 0)
    assert(result.confidence > 0)

  test("PricePredictor should process batch predictions"):
    val predictor = PricePredictor(LinearRegressionStrategy())
    val properties = List(
      Property(100, 3, "Centro", "Curitiba", 5),
      Property(80, 2, "Jardins", "Sao Paulo", 3)
    )
    val results = predictor.predictBatch(properties)
    assert(results.length == 2)
    assert(results.forall(_.estimatedPrice > 0))

  test("Property should store all property data"):
    val property = Property(120, 4, "Batel", "Curitiba", 2)
    assert(property.area == 120)
    assert(property.rooms == 4)
    assert(property.neighborhood == "Batel")
    assert(property.city == "Curitiba")
    assert(property.age == 2)
