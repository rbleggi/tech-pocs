package com.rbleggi.sealedtraits

import org.scalatest.funsuite.AnyFunSuite

class SealedTraitsSpec extends AnyFunSuite:

  test("Pix should process payment correctly"):
    val pix = Pix("joao@example.com")
    val result = pix.process(100.0)
    assert(result == "PIX payment of R$ 100.00 to key joao@example.com")

  test("Pix should have zero fee"):
    val pix = Pix("maria@example.com")
    val fee = PaymentProcessor.calculateFee(pix, 100.0)
    assert(fee == 0.0)

  test("BankSlip should process payment correctly"):
    val slip = BankSlip("23793381286000000012345678901234567890123456")
    val result = slip.process(250.50)
    assert(result == "Bank slip generated of R$ 250.50 - Code: 23793381286000000012345678901234567890123456")

  test("BankSlip should calculate 1% fee"):
    val slip = BankSlip("12345678901234567890123456789012345678901234")
    val fee = PaymentProcessor.calculateFee(slip, 100.0)
    assert(fee == 1.0)

  test("CreditCard should process payment with installments"):
    val card = CreditCard("1234567890123456", 3)
    val result = card.process(450.0)
    assert(result == "Credit card ending 3456 - 3x of R$ 150.00")

  test("CreditCard should calculate 3% fee"):
    val card = CreditCard("1234567890123456", 2)
    val fee = PaymentProcessor.calculateFee(card, 100.0)
    assert(fee == 3.0)

  test("DebitCard should process payment correctly"):
    val card = DebitCard("9876543210987654")
    val result = card.process(80.0)
    assert(result == "Debit on card ending 7654 - R$ 80.00")

  test("DebitCard should calculate 1.5% fee"):
    val card = DebitCard("9876543210987654")
    val fee = PaymentProcessor.calculateFee(card, 100.0)
    assert(fee == 1.5)

  test("PaymentProcessor should process Pix through pattern matching"):
    val pix = Pix("carlos@example.com")
    val result = PaymentProcessor.process(pix, 200.0)
    assert(result.contains("PIX"))
    assert(result.contains("200.00"))

  test("PaymentProcessor should process BankSlip through pattern matching"):
    val slip = BankSlip("99999999999999999999999999999999999999999999")
    val result = PaymentProcessor.process(slip, 150.0)
    assert(result.contains("Bank slip"))
    assert(result.contains("150.00"))
