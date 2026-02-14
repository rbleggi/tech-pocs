package com.rbleggi.typeclasses

import org.scalatest.funsuite.AnyFunSuite
import java.time.LocalDate

class TypeClassesSpec extends AnyFunSuite:

  test("Double formatter should format currency in BRL"):
    val preco = 1250.50
    val resultado = preco.formatar
    assert(resultado == "R$ 1250.50")

  test("Double formatter should format zero correctly"):
    val preco = 0.0
    val resultado = preco.formatar
    assert(resultado == "R$ 0.00")

  test("LocalDate formatter should format in Brazilian format"):
    val data = LocalDate.of(2024, 2, 14)
    val resultado = data.formatar
    assert(resultado == "14/02/2024")

  test("LocalDate formatter should format different date"):
    val data = LocalDate.of(2023, 12, 31)
    val resultado = data.formatar
    assert(resultado == "31/12/2023")

  test("String formatter should convert to uppercase"):
    val nome = "joão silva"
    val resultado = nome.formatar
    assert(resultado == "JOÃO SILVA")

  test("CPF formatter should format numeric CPF"):
    val cpf = Cpf("12345678901")
    val resultado = cpf.formatar
    assert(resultado == "123.456.789-01")

  test("CPF formatter should format another CPF"):
    val cpf = Cpf("98765432100")
    val resultado = cpf.formatar
    assert(resultado == "987.654.321-00")

  test("CPF formatter should handle already formatted CPF"):
    val cpf = Cpf("123.456.789-01")
    val resultado = cpf.formatar
    assert(resultado == "123.456.789-01")

  test("Produto formatter should format product with price"):
    val produto = Produto("Notebook Dell", 3500.00, "Eletrônicos")
    val resultado = produto.formatar
    assert(resultado == "Notebook Dell - R$ 3500.00 (Eletrônicos)")

  test("Produto formatter should format different product"):
    val produto = Produto("Mouse", 45.90, "Acessórios")
    val resultado = produto.formatar
    assert(resultado == "Mouse - R$ 45.90 (Acessórios)")

  test("Formatador.formatar should work with using clause"):
    val preco = 999.99
    val resultado = Formatador.formatar(preco)
    assert(resultado == "R$ 999.99")

  test("Context bound should work correctly"):
    def exibir[T: Formatador](value: T): String =
      Formatador.formatar(value)

    val resultado = exibir(1500.75)
    assert(resultado == "R$ 1500.75")
