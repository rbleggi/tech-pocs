package com.rbleggi.realestate

case class Property(
  area: Double,
  rooms: Int,
  neighborhood: String,
  city: String,
  age: Int
)

case class Prediction(property: Property, estimatedPrice: Double, confidence: Double)

trait PredictionStrategy:
  def predict(property: Property): (Double, Double)

class LinearRegressionStrategy extends PredictionStrategy:
  private val areaWeight = 5000.0
  private val roomsWeight = 80000.0
  private val ageWeight = -2000.0

  private val neighborhoodMultiplier = Map(
    "Jardins" -> 1.5,
    "Vila Madalena" -> 1.3,
    "Centro" -> 1.0,
    "Perdizes" -> 1.2,
    "Batel" -> 1.4
  )

  private val cityMultiplier = Map(
    "Sao Paulo" -> 1.2,
    "Curitiba" -> 1.0,
    "Belo Horizonte" -> 0.9
  )

  def predict(property: Property): (Double, Double) =
    val basePrice = (
      property.area * areaWeight +
      property.rooms * roomsWeight +
      property.age * ageWeight
    )

    val multNeighborhood = neighborhoodMultiplier.getOrElse(property.neighborhood, 1.0)
    val multCity = cityMultiplier.getOrElse(property.city, 1.0)

    val price = basePrice * multNeighborhood * multCity
    val confidence = 0.75

    (price, confidence)

class KNNStrategy extends PredictionStrategy:
  private val trainingBase = List(
    (Property(80, 2, "Jardins", "Sao Paulo", 5), 650000.0),
    (Property(120, 3, "Vila Madalena", "Sao Paulo", 3), 850000.0),
    (Property(70, 2, "Centro", "Curitiba", 10), 380000.0),
    (Property(150, 4, "Batel", "Curitiba", 2), 720000.0),
    (Property(90, 3, "Centro", "Belo Horizonte", 8), 420000.0)
  )

  private def distance(p1: Property, p2: Property): Double =
    val areaGap = (p1.area - p2.area) / 100.0
    val roomsGap = (p1.rooms - p2.rooms) * 10.0
    val ageGap = (p1.age - p2.age) / 5.0
    val cityGap = if p1.city == p2.city then 0.0 else 50.0

    Math.sqrt(
      areaGap * areaGap +
      roomsGap * roomsGap +
      ageGap * ageGap +
      cityGap * cityGap
    )

  def predict(property: Property): (Double, Double) =
    val k = 3
    val neighbors = trainingBase
      .map { case (baseProperty, price) => (baseProperty, price, distance(property, baseProperty)) }
      .sortBy(_._3)
      .take(k)

    val averagePrice = neighbors.map(_._2).sum / k
    val confidence = 0.82

    (averagePrice, confidence)

class EnsembleStrategy extends PredictionStrategy:
  private val linear = LinearRegressionStrategy()
  private val knn = KNNStrategy()

  def predict(property: Property): (Double, Double) =
    val (price1, conf1) = linear.predict(property)
    val (price2, conf2) = knn.predict(property)

    val linearWeight = conf1 / (conf1 + conf2)
    val knnWeight = conf2 / (conf1 + conf2)

    val finalPrice = price1 * linearWeight + price2 * knnWeight
    val finalConfidence = (conf1 + conf2) / 2

    (finalPrice, finalConfidence)

class PricePredictor(strategy: PredictionStrategy):
  def predict(property: Property): Prediction =
    val (price, confidence) = strategy.predict(property)
    Prediction(property, price, confidence)

  def predictBatch(properties: List[Property]): List[Prediction] =
    properties.map(predict)

@main def run(): Unit =
  println("Real Estate Price Prediction")
