package com.rbleggi.realestate

case class Imovel(
  area: Double,
  quartos: Int,
  bairro: String,
  cidade: String,
  idade: Int
)

case class Predicao(imovel: Imovel, precoEstimado: Double, confianca: Double)

trait PredictionStrategy:
  def prever(imovel: Imovel): (Double, Double)

class LinearRegressionStrategy extends PredictionStrategy:
  private val pesoArea = 5000.0
  private val pesoQuartos = 80000.0
  private val pesoIdade = -2000.0

  private val multiplicadorBairro = Map(
    "Jardins" -> 1.5,
    "Vila Madalena" -> 1.3,
    "Centro" -> 1.0,
    "Perdizes" -> 1.2,
    "Batel" -> 1.4
  )

  private val multiplicadorCidade = Map(
    "Sao Paulo" -> 1.2,
    "Curitiba" -> 1.0,
    "Belo Horizonte" -> 0.9
  )

  def prever(imovel: Imovel): (Double, Double) =
    val precoBase = (
      imovel.area * pesoArea +
      imovel.quartos * pesoQuartos +
      imovel.idade * pesoIdade
    )

    val multBairro = multiplicadorBairro.getOrElse(imovel.bairro, 1.0)
    val multCidade = multiplicadorCidade.getOrElse(imovel.cidade, 1.0)

    val preco = precoBase * multBairro * multCidade
    val confianca = 0.75

    (preco, confianca)

class KNNStrategy extends PredictionStrategy:
  private val baseTreinamento = List(
    (Imovel(80, 2, "Jardins", "Sao Paulo", 5), 650000.0),
    (Imovel(120, 3, "Vila Madalena", "Sao Paulo", 3), 850000.0),
    (Imovel(70, 2, "Centro", "Curitiba", 10), 380000.0),
    (Imovel(150, 4, "Batel", "Curitiba", 2), 720000.0),
    (Imovel(90, 3, "Centro", "Belo Horizonte", 8), 420000.0)
  )

  private def distancia(i1: Imovel, i2: Imovel): Double =
    val difArea = (i1.area - i2.area) / 100.0
    val difQuartos = (i1.quartos - i2.quartos) * 10.0
    val difIdade = (i1.idade - i2.idade) / 5.0
    val difCidade = if i1.cidade == i2.cidade then 0.0 else 50.0

    Math.sqrt(
      difArea * difArea +
      difQuartos * difQuartos +
      difIdade * difIdade +
      difCidade * difCidade
    )

  def prever(imovel: Imovel): (Double, Double) =
    val k = 3
    val vizinhos = baseTreinamento
      .map { case (imovelBase, preco) => (imovelBase, preco, distancia(imovel, imovelBase)) }
      .sortBy(_._3)
      .take(k)

    val precoMedio = vizinhos.map(_._2).sum / k
    val confianca = 0.82

    (precoMedio, confianca)

class EnsembleStrategy extends PredictionStrategy:
  private val linear = LinearRegressionStrategy()
  private val knn = KNNStrategy()

  def prever(imovel: Imovel): (Double, Double) =
    val (preco1, conf1) = linear.prever(imovel)
    val (preco2, conf2) = knn.prever(imovel)

    val pesoLinear = conf1 / (conf1 + conf2)
    val pesoKNN = conf2 / (conf1 + conf2)

    val precoFinal = preco1 * pesoLinear + preco2 * pesoKNN
    val confiancaFinal = (conf1 + conf2) / 2

    (precoFinal, confiancaFinal)

class PrevisaoPreco(strategy: PredictionStrategy):
  def prever(imovel: Imovel): Predicao =
    val (preco, confianca) = strategy.prever(imovel)
    Predicao(imovel, preco, confianca)

  def preverLote(imoveis: List[Imovel]): List[Predicao] =
    imoveis.map(prever)

@main def run(): Unit =
  val imoveis = List(
    Imovel(100, 3, "Jardins", "Sao Paulo", 4),
    Imovel(75, 2, "Centro", "Curitiba", 12),
    Imovel(130, 4, "Batel", "Curitiba", 1),
    Imovel(85, 2, "Centro", "Belo Horizonte", 7)
  )

  println("=== Linear Regression Strategy ===")
  val previsaoLinear = PrevisaoPreco(LinearRegressionStrategy())
  previsaoLinear.preverLote(imoveis).foreach { pred =>
    println(f"${pred.imovel.cidade} - ${pred.imovel.bairro} (${pred.imovel.area}%.0fm²): R$$ ${pred.precoEstimado}%,.2f (${pred.confianca}%.0f%%)")
  }
  println()

  println("=== KNN Strategy ===")
  val previsaoKNN = PrevisaoPreco(KNNStrategy())
  previsaoKNN.preverLote(imoveis).foreach { pred =>
    println(f"${pred.imovel.cidade} - ${pred.imovel.bairro} (${pred.imovel.area}%.0fm²): R$$ ${pred.precoEstimado}%,.2f (${pred.confianca}%.0f%%)")
  }
  println()

  println("=== Ensemble Strategy ===")
  val previsaoEnsemble = PrevisaoPreco(EnsembleStrategy())
  previsaoEnsemble.preverLote(imoveis).foreach { pred =>
    println(f"${pred.imovel.cidade} - ${pred.imovel.bairro} (${pred.imovel.area}%.0fm²): R$$ ${pred.precoEstimado}%,.2f (${pred.confianca}%.0f%%)")
  }
