package com.rbleggi.sealedtraits

sealed trait FormaPagamento:
  def processar(valor: Double): String

case class Pix(chave: String) extends FormaPagamento:
  def processar(valor: Double): String =
    f"Pagamento via PIX de R$$ $valor%.2f para chave $chave"

case class Boleto(codigoBarras: String) extends FormaPagamento:
  def processar(valor: Double): String =
    f"Boleto gerado de R$$ $valor%.2f - Código: $codigoBarras"

case class CartaoCredito(numero: String, parcelas: Int) extends FormaPagamento:
  def processar(valor: Double): String =
    val valorParcela = valor / parcelas
    f"Cartão de crédito final ${numero.takeRight(4)} - ${parcelas}x de R$$ $valorParcela%.2f"

case class CartaoDebito(numero: String) extends FormaPagamento:
  def processar(valor: Double): String =
    f"Débito no cartão final ${numero.takeRight(4)} - R$$ $valor%.2f"

object ProcessadorPagamento:
  def processar(pagamento: FormaPagamento, valor: Double): String =
    pagamento match
      case pix: Pix => pix.processar(valor)
      case boleto: Boleto => boleto.processar(valor)
      case cc: CartaoCredito => cc.processar(valor)
      case cd: CartaoDebito => cd.processar(valor)

  def calcularTaxa(pagamento: FormaPagamento, valor: Double): Double =
    pagamento match
      case _: Pix => 0.0
      case _: Boleto => valor * 0.01
      case _: CartaoCredito => valor * 0.03
      case _: CartaoDebito => valor * 0.015

@main def run(): Unit =
  val pagamentos = List(
    Pix("joao@example.com"),
    Boleto("23793381286000000012345678901234567890123456"),
    CartaoCredito("1234567890123456", 3),
    CartaoDebito("9876543210987654")
  )

  val valores = List(100.0, 250.50, 450.0, 80.0)

  pagamentos.zip(valores).foreach { (pag, valor) =>
    val processamento = ProcessadorPagamento.processar(pag, valor)
    val taxa = ProcessadorPagamento.calcularTaxa(pag, valor)
    println(processamento)
    println(f"Taxa: R$$ $taxa%.2f")
    println()
  }
