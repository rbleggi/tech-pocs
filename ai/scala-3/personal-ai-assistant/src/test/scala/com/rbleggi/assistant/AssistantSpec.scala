package com.rbleggi.assistant

import org.scalatest.funsuite.AnyFunSuite

class AssistantSpec extends AnyFunSuite:
  test("ManipuladorMatematica deve somar numeros"):
    val manipulador = ManipuladorMatematica()
    val consulta = Consulta("matematica", "Qual a soma de 10 e 20?")
    val resposta = manipulador.processar(consulta)

    assert(resposta.tipo == "matematica")
    assert(resposta.resposta.contains("30"))
    assert(resposta.confianca == 1.0)

  test("ManipuladorMatematica deve multiplicar numeros"):
    val manipulador = ManipuladorMatematica()
    val consulta = Consulta("matematica", "Multiplica 5 e 4")
    val resposta = manipulador.processar(consulta)

    assert(resposta.resposta.contains("20"))

  test("ManipuladorMatematica deve lidar com erro"):
    val manipulador = ManipuladorMatematica()
    val consulta = Consulta("matematica", "soma sem numeros")
    val resposta = manipulador.processar(consulta)

    assert(resposta.confianca < 1.0)

  test("ManipuladorTexto deve contar palavras"):
    val manipulador = ManipuladorTexto()
    val consulta = Consulta("texto", "Quantas palavras tem nesta frase?")
    val resposta = manipulador.processar(consulta)

    assert(resposta.tipo == "texto")
    assert(resposta.resposta.contains("5 palavras"))

  test("ManipuladorTexto deve converter para maiuscula"):
    val manipulador = ManipuladorTexto()
    val consulta = Consulta("texto", "Converta para maiuscula: joao")
    val resposta = manipulador.processar(consulta)

    assert(resposta.resposta.contains("JOAO"))
    assert(resposta.confianca == 1.0)

  test("ManipuladorTexto deve converter para minuscula"):
    val manipulador = ManipuladorTexto()
    val consulta = Consulta("texto", "Converta para minuscula: MARIA")
    val resposta = manipulador.processar(consulta)

    assert(resposta.resposta.toLowerCase.contains("maria"))

  test("ManipuladorDados deve encontrar Sao Paulo"):
    val manipulador = ManipuladorDados()
    val consulta = Consulta("dados", "Informacoes sobre sao paulo")
    val resposta = manipulador.processar(consulta)

    assert(resposta.tipo == "dados")
    assert(resposta.resposta.contains("12 milhoes"))
    assert(resposta.confianca > 0.9)

  test("ManipuladorDados deve encontrar Curitiba"):
    val manipulador = ManipuladorDados()
    val consulta = Consulta("dados", "O que voce sabe sobre curitiba?")
    val resposta = manipulador.processar(consulta)

    assert(resposta.resposta.contains("1.9 milhoes"))

  test("ManipuladorDados deve retornar nao encontrado"):
    val manipulador = ManipuladorDados()
    val consulta = Consulta("dados", "Informacoes sobre xyz")
    val resposta = manipulador.processar(consulta)

    assert(resposta.resposta.contains("nao encontrada"))

  test("AssistentePessoal deve processar consulta matematica"):
    val assistente = AssistentePessoal()
    val consulta = Consulta("matematica", "soma 15 e 25")
    val resposta = assistente.consultar(consulta)

    assert(resposta.tipo == "matematica")

  test("AssistentePessoal deve processar consulta de texto"):
    val assistente = AssistentePessoal()
    val consulta = Consulta("texto", "quantas palavras tem aqui")
    val resposta = assistente.consultar(consulta)

    assert(resposta.tipo == "texto")
