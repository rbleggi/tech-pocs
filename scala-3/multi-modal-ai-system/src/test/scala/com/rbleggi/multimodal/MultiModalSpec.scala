package com.rbleggi.multimodal

import org.scalatest.funsuite.AnyFunSuite

class MultiModalSpec extends AnyFunSuite:
  test("ProcessadorTexto deve analisar texto positivo"):
    val processador = ProcessadorTexto()
    val entrada = EntradaTexto("O servico e muito bom")
    val analise = processador.processar(entrada)

    assert(analise.tipo == "texto")
    assert(analise.resultado.contains("positivo"))
    assert(analise.confianca > 0.8)

  test("ProcessadorTexto deve analisar texto negativo"):
    val processador = ProcessadorTexto()
    val entrada = EntradaTexto("O atendimento foi ruim")
    val analise = processador.processar(entrada)

    assert(analise.resultado.contains("negativo"))

  test("ProcessadorTexto deve contar palavras"):
    val processador = ProcessadorTexto()
    val entrada = EntradaTexto("Joao Maria Carlos")
    val analise = processador.processar(entrada)

    assert(analise.resultado.contains("3 palavras"))

  test("ProcessadorNumero deve classificar numero pequeno"):
    val processador = ProcessadorNumero()
    val entrada = EntradaNumero(50.0)
    val analise = processador.processar(entrada)

    assert(analise.tipo == "numero")
    assert(analise.resultado.contains("pequeno"))

  test("ProcessadorNumero deve classificar numero grande"):
    val processador = ProcessadorNumero()
    val entrada = EntradaNumero(5000.0)
    val analise = processador.processar(entrada)

    assert(analise.resultado.contains("grande"))

  test("ProcessadorCategoria deve identificar SP"):
    val processador = ProcessadorCategoria()
    val entrada = EntradaCategoria("sp")
    val analise = processador.processar(entrada)

    assert(analise.tipo == "categoria")
    assert(analise.resultado.contains("Sao Paulo"))
    assert(analise.confianca == 1.0)

  test("ProcessadorCategoria deve identificar categoria desconhecida"):
    val processador = ProcessadorCategoria()
    val entrada = EntradaCategoria("xyz")
    val analise = processador.processar(entrada)

    assert(analise.resultado.contains("desconhecido"))

  test("SistemaMultiModal deve processar texto"):
    val sistema = SistemaMultiModal()
    val entrada = EntradaTexto("teste")
    val analise = sistema.processar(entrada, "texto")

    assert(analise.tipo == "texto")

  test("SistemaMultiModal deve processar numero"):
    val sistema = SistemaMultiModal()
    val entrada = EntradaNumero(100.0)
    val analise = sistema.processar(entrada, "numero")

    assert(analise.tipo == "numero")

  test("SistemaMultiModal deve retornar erro para modo desconhecido"):
    val sistema = SistemaMultiModal()
    val entrada = EntradaTexto("teste")
    val analise = sistema.processar(entrada, "invalido")

    assert(analise.tipo == "erro")
