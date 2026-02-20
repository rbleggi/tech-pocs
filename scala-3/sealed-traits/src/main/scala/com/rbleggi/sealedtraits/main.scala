package com.rbleggi.sealedtraits

sealed trait PaymentMethod:
  def process(amount: Double): String

case class Pix(key: String) extends PaymentMethod:
  def process(amount: Double): String =
    f"PIX payment of R$$ $amount%.2f to key $key"

case class BankSlip(barcode: String) extends PaymentMethod:
  def process(amount: Double): String =
    f"Bank slip generated of R$$ $amount%.2f - Code: $barcode"

case class CreditCard(number: String, installments: Int) extends PaymentMethod:
  def process(amount: Double): String =
    val installmentValue = amount / installments
    f"Credit card ending ${number.takeRight(4)} - ${installments}x of R$$ $installmentValue%.2f"

case class DebitCard(number: String) extends PaymentMethod:
  def process(amount: Double): String =
    f"Debit on card ending ${number.takeRight(4)} - R$$ $amount%.2f"

object PaymentProcessor:
  def process(payment: PaymentMethod, amount: Double): String =
    payment match
      case pix: Pix => pix.process(amount)
      case slip: BankSlip => slip.process(amount)
      case cc: CreditCard => cc.process(amount)
      case dc: DebitCard => dc.process(amount)

  def calculateFee(payment: PaymentMethod, amount: Double): Double =
    payment match
      case _: Pix => 0.0
      case _: BankSlip => amount * 0.01
      case _: CreditCard => amount * 0.03
      case _: DebitCard => amount * 0.015

@main def run(): Unit =
  val payments = List(
    Pix("joao@example.com"),
    BankSlip("23793381286000000012345678901234567890123456"),
    CreditCard("1234567890123456", 3),
    DebitCard("9876543210987654")
  )

  val amounts = List(100.0, 250.50, 450.0, 80.0)

  payments.zip(amounts).foreach { (payment, amount) =>
    val processing = PaymentProcessor.process(payment, amount)
    val fee = PaymentProcessor.calculateFee(payment, amount)
    println(processing)
    println(f"Fee: R$$ $fee%.2f")
    println()
  }
