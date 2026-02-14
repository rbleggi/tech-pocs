package com.rbleggi.typeclasses

import java.time.LocalDate
import java.time.format.DateTimeFormatter

trait Formatador[T]:
  def formatar(value: T): String

object Formatador:
  def formatar[T](value: T)(using f: Formatador[T]): String =
    f.formatar(value)

  given Formatador[Double] with
    def formatar(value: Double): String =
      f"R$$ ${value}%.2f"

  given Formatador[LocalDate] with
    def formatar(value: LocalDate): String =
      val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
      value.format(formatter)

  given Formatador[String] with
    def formatar(value: String): String =
      value.toUpperCase

case class Cpf(numero: String)

object Cpf:
  given Formatador[Cpf] with
    def formatar(cpf: Cpf): String =
      val limpo = cpf.numero.replaceAll("[^0-9]", "")
      if limpo.length == 11 then
        s"${limpo.substring(0, 3)}.${limpo.substring(3, 6)}.${limpo.substring(6, 9)}-${limpo.substring(9, 11)}"
      else
        cpf.numero

case class Produto(nome: String, preco: Double, categoria: String)

object Produto:
  given Formatador[Produto] with
    def formatar(produto: Produto): String =
      val precoFormatado = Formatador.formatar(produto.preco)
      s"${produto.nome} - $precoFormatado (${produto.categoria})"

extension [T](value: T)
  def formatar(using f: Formatador[T]): String =
    f.formatar(value)

@main def run(): Unit =
  val preco = 1250.50
  println(s"Preço: ${preco.formatar}")

  val data = LocalDate.of(2024, 2, 14)
  println(s"Data: ${data.formatar}")

  val cpf = Cpf("12345678901")
  println(s"CPF: ${cpf.formatar}")

  val produto = Produto("Notebook Dell", 3500.00, "Eletrônicos")
  println(s"Produto: ${produto.formatar}")

  val nome = "joão silva"
  println(s"Nome: ${nome.formatar}")

  println()

  def exibir[T: Formatador](label: String, value: T): Unit =
    println(s"$label: ${Formatador.formatar(value)}")

  exibir("Valor", 999.99)
  exibir("Vencimento", LocalDate.of(2024, 12, 31))
  exibir("Documento", Cpf("98765432100"))
