package com.rbleggi.typeclasses

import java.time.LocalDate
import java.time.format.DateTimeFormatter

trait Formatter[T]:
  def format(value: T): String

object Formatter:
  def format[T](value: T)(using f: Formatter[T]): String =
    f.format(value)

  given Formatter[Double] with
    def format(value: Double): String =
      f"R$$ ${value}%.2f"

  given Formatter[LocalDate] with
    def format(value: LocalDate): String =
      val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
      value.format(formatter)

  given Formatter[String] with
    def format(value: String): String =
      value.toUpperCase

case class Cpf(number: String)

object Cpf:
  given Formatter[Cpf] with
    def format(cpf: Cpf): String =
      val clean = cpf.number.replaceAll("[^0-9]", "")
      if clean.length == 11 then
        s"${clean.substring(0, 3)}.${clean.substring(3, 6)}.${clean.substring(6, 9)}-${clean.substring(9, 11)}"
      else
        cpf.number

case class Product(name: String, price: Double, category: String)

object Product:
  given Formatter[Product] with
    def format(product: Product): String =
      val formattedPrice = Formatter.format(product.price)
      s"${product.name} - $formattedPrice (${product.category})"

extension [T](value: T)
  def display(using f: Formatter[T]): String =
    f.format(value)

@main def run(): Unit =
  val price = 1250.50
  println(s"Price: ${price.display}")

  val date = LocalDate.of(2024, 2, 14)
  println(s"Date: ${date.display}")

  val cpf = Cpf("12345678901")
  println(s"CPF: ${cpf.display}")

  val product = Product("Notebook Dell", 3500.00, "Electronics")
  println(s"Product: ${product.display}")

  val name = "joao silva"
  println(s"Name: ${name.display}")

  println()

  def display[T: Formatter](label: String, value: T): Unit =
    println(s"$label: ${Formatter.format(value)}")

  display("Value", 999.99)
  display("Due Date", LocalDate.of(2024, 12, 31))
  display("Document", Cpf("98765432100"))
