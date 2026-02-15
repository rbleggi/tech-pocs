package com.rbleggi.codegen

import org.scalatest.funsuite.AnyFunSuite

class CodeGenerationSpec extends AnyFunSuite:
  test("GeradorClasse deve gerar classe Pessoa"):
    val template = Template(
      "ClassePessoa",
      "case class {{nomeClasse}}(nome: String, cpf: String)"
    )
    val gerador = GeradorClasse()
    val parametros = Map("nomeClasse" -> "Pessoa")
    val codigo = gerador.gerar(template, parametros)

    assert(codigo.arquivo == "Pessoa.scala")
    assert(codigo.conteudo.contains("case class Pessoa"))

  test("GeradorClasse deve gerar classe Produto"):
    val template = Template(
      "ClasseProduto",
      "case class {{nomeClasse}}(nome: String, preco: Double)"
    )
    val gerador = GeradorClasse()
    val parametros = Map("nomeClasse" -> "Produto")
    val codigo = gerador.gerar(template, parametros)

    assert(codigo.arquivo == "Produto.scala")
    assert(codigo.conteudo.contains("case class Produto"))

  test("GeradorFuncao deve gerar funcao calcular"):
    val template = Template(
      "FuncaoCalcular",
      "def {{nomeFuncao}}(a: Int, b: Int): Int = a + b"
    )
    val gerador = GeradorFuncao()
    val parametros = Map("nomeFuncao" -> "somar")
    val codigo = gerador.gerar(template, parametros)

    assert(codigo.arquivo == "somar.scala")
    assert(codigo.conteudo.contains("def somar"))

  test("GeradorTeste deve gerar teste para Pessoa"):
    val template = Template(
      "TestePessoa",
      """test("{{nomeClasse}} deve criar instancia"):
        |  val obj = {{nomeClasse}}("Joao", "12345678900")
        |  assert(obj.nome == "Joao")
        |""".stripMargin
    )
    val gerador = GeradorTeste()
    val parametros = Map("nomeClasse" -> "Pessoa")
    val codigo = gerador.gerar(template, parametros)

    assert(codigo.arquivo == "PessoaSpec.scala")
    assert(codigo.conteudo.contains("Pessoa deve criar instancia"))

  test("AssistenteGeracao deve usar GeradorClasse"):
    val template = Template("Classe", "class {{nomeClasse}}")
    val gerador = GeradorClasse()
    val assistente = AssistenteGeracao(gerador)
    val parametros = Map("nomeClasse" -> "Cliente")
    val codigo = assistente.gerarCodigo(template, parametros)

    assert(codigo.conteudo.contains("class Cliente"))

  test("AssistenteGeracao deve usar GeradorFuncao"):
    val template = Template("Funcao", "def {{nomeFuncao}}(): Unit = ()")
    val gerador = GeradorFuncao()
    val assistente = AssistenteGeracao(gerador)
    val parametros = Map("nomeFuncao" -> "processar")
    val codigo = assistente.gerarCodigo(template, parametros)

    assert(codigo.conteudo.contains("def processar"))
