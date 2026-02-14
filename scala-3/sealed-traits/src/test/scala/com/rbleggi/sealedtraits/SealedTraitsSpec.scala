package com.rbleggi.sealedtraits

import org.scalatest.funsuite.AnyFunSuite

class SealedTraitsSpec extends AnyFunSuite:

  test("Pix should process payment correctly"):
    val pix = Pix("joao@example.com")
    val resultado = pix.processar(100.0)
    assert(resultado == "Pagamento via PIX de R$ 100.00 para chave joao@example.com")

  test("Pix should have zero fee"):
    val pix = Pix("maria@example.com")
    val taxa = ProcessadorPagamento.calcularTaxa(pix, 100.0)
    assert(taxa == 0.0)

  test("Boleto should process payment correctly"):
    val boleto = Boleto("23793381286000000012345678901234567890123456")
    val resultado = boleto.processar(250.50)
    assert(resultado == "Boleto gerado de R$ 250.50 - Código: 23793381286000000012345678901234567890123456")

  test("Boleto should calculate 1% fee"):
    val boleto = Boleto("12345678901234567890123456789012345678901234")
    val taxa = ProcessadorPagamento.calcularTaxa(boleto, 100.0)
    assert(taxa == 1.0)

  test("CartaoCredito should process payment with installments"):
    val cartao = CartaoCredito("1234567890123456", 3)
    val resultado = cartao.processar(450.0)
    assert(resultado == "Cartão de crédito final 3456 - 3x de R$ 150.00")

  test("CartaoCredito should calculate 3% fee"):
    val cartao = CartaoCredito("1234567890123456", 2)
    val taxa = ProcessadorPagamento.calcularTaxa(cartao, 100.0)
    assert(taxa == 3.0)

  test("CartaoDebito should process payment correctly"):
    val cartao = CartaoDebito("9876543210987654")
    val resultado = cartao.processar(80.0)
    assert(resultado == "Débito no cartão final 7654 - R$ 80.00")

  test("CartaoDebito should calculate 1.5% fee"):
    val cartao = CartaoDebito("9876543210987654")
    val taxa = ProcessadorPagamento.calcularTaxa(cartao, 100.0)
    assert(taxa == 1.5)

  test("ProcessadorPagamento should process Pix through pattern matching"):
    val pix = Pix("carlos@example.com")
    val resultado = ProcessadorPagamento.processar(pix, 200.0)
    assert(resultado.contains("PIX"))
    assert(resultado.contains("200.00"))

  test("ProcessadorPagamento should process Boleto through pattern matching"):
    val boleto = Boleto("99999999999999999999999999999999999999999999")
    val resultado = ProcessadorPagamento.processar(boleto, 150.0)
    assert(resultado.contains("Boleto"))
    assert(resultado.contains("150.00"))
