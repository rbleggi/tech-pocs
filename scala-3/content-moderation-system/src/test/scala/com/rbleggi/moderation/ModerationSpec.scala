package com.rbleggi.moderation

import org.scalatest.funsuite.AnyFunSuite

class ModerationSpec extends AnyFunSuite:
  test("FiltroKeywords deve bloquear spam"):
    val filtro = FiltroKeywords()
    val conteudo = Conteudo("1", "Isso e um spam barato", "Joao")
    val resultado = filtro.moderar(conteudo)

    assert(!resultado.aprovado)
    assert(resultado.motivo.contains("spam"))
    assert(resultado.severidade == "alta")

  test("FiltroKeywords deve bloquear golpe"):
    val filtro = FiltroKeywords()
    val conteudo = Conteudo("2", "Venha participar deste golpe", "Maria")
    val resultado = filtro.moderar(conteudo)

    assert(!resultado.aprovado)
    assert(resultado.motivo.contains("golpe"))

  test("FiltroKeywords deve aprovar texto normal"):
    val filtro = FiltroKeywords()
    val conteudo = Conteudo("3", "Texto completamente normal", "Carlos")
    val resultado = filtro.moderar(conteudo)

    assert(resultado.aprovado)

  test("FiltroRegex deve detectar CPF"):
    val filtro = FiltroRegex()
    val conteudo = Conteudo("4", "Meu CPF e 123.456.789-00", "Ana")
    val resultado = filtro.moderar(conteudo)

    assert(!resultado.aprovado)
    assert(resultado.motivo.contains("dados sensiveis"))

  test("FiltroRegex deve detectar valor em reais"):
    val filtro = FiltroRegex()
    val conteudo = Conteudo("5", "O preco e R$ 150", "Pedro")
    val resultado = filtro.moderar(conteudo)

    assert(!resultado.aprovado)

  test("FiltroRegex deve aprovar texto sem dados sensiveis"):
    val filtro = FiltroRegex()
    val conteudo = Conteudo("6", "Mensagem sem informacoes pessoais", "Lucas")
    val resultado = filtro.moderar(conteudo)

    assert(resultado.aprovado)

  test("FiltroTamanho deve bloquear texto curto"):
    val filtro = FiltroTamanho()
    val conteudo = Conteudo("7", "Oi", "Joao")
    val resultado = filtro.moderar(conteudo)

    assert(!resultado.aprovado)
    assert(resultado.motivo.contains("muito curto"))

  test("FiltroTamanho deve aprovar texto adequado"):
    val filtro = FiltroTamanho()
    val conteudo = Conteudo("8", "Texto de tamanho adequado para moderacao", "Maria")
    val resultado = filtro.moderar(conteudo)

    assert(resultado.aprovado)

  test("SistemaModeracao deve aprovar conteudo valido"):
    val sistema = SistemaModeracao()
    val conteudo = Conteudo("9", "Mensagem valida de tamanho adequado", "Carlos")

    assert(sistema.aprovar(conteudo))

  test("SistemaModeracao deve rejeitar conteudo com spam"):
    val sistema = SistemaModeracao()
    val conteudo = Conteudo("10", "Isso e spam de golpe", "Ana")

    assert(!sistema.aprovar(conteudo))
