package com.rbleggi.codegen

import org.scalatest.funsuite.AnyFunSuite

class CodeGenerationSpec extends AnyFunSuite:
  test("GeradorClasse deve gerar classe Pessoa"):
    val template = Template(
      "ClassePessoa",
      "case class {{nomeClasse}}(nome: String, cpf: String)"
    )
    val generator = ClassGenerator()
    val parameters = Map("nomeClasse" -> "Pessoa")
    val code = generator.generate(template, parameters)

    assert(code.file == "Pessoa.scala")
    assert(code.content.contains("case class Pessoa"))

  test("GeradorClasse deve gerar classe Produto"):
    val template = Template(
      "ClasseProduto",
      "case class {{nomeClasse}}(nome: String, preco: Double)"
    )
    val generator = ClassGenerator()
    val parameters = Map("nomeClasse" -> "Produto")
    val code = generator.generate(template, parameters)

    assert(code.file == "Produto.scala")
    assert(code.content.contains("case class Produto"))

  test("GeradorFuncao deve gerar funcao calcular"):
    val template = Template(
      "FuncaoCalcular",
      "def {{nomeFuncao}}(a: Int, b: Int): Int = a + b"
    )
    val generator = FunctionGenerator()
    val parameters = Map("nomeFuncao" -> "somar")
    val code = generator.generate(template, parameters)

    assert(code.file == "somar.scala")
    assert(code.content.contains("def somar"))

  test("GeradorTeste deve gerar teste para Pessoa"):
    val template = Template(
      "TestePessoa",
      """test("{{nomeClasse}} deve criar instancia"):
        |  val obj = {{nomeClasse}}("Joao", "12345678900")
        |  assert(obj.nome == "Joao")
        |""".stripMargin
    )
    val generator = TestGenerator()
    val parameters = Map("nomeClasse" -> "Pessoa")
    val code = generator.generate(template, parameters)

    assert(code.file == "PessoaSpec.scala")
    assert(code.content.contains("Pessoa deve criar instancia"))

  test("AssistenteGeracao deve usar GeradorClasse"):
    val template = Template("Classe", "class {{nomeClasse}}")
    val generator = ClassGenerator()
    val assistant = CodeAssistant(generator)
    val parameters = Map("nomeClasse" -> "Cliente")
    val code = assistant.generateCode(template, parameters)

    assert(code.content.contains("class Cliente"))

  test("AssistenteGeracao deve usar GeradorFuncao"):
    val template = Template("Funcao", "def {{nomeFuncao}}(): Unit = ()")
    val generator = FunctionGenerator()
    val assistant = CodeAssistant(generator)
    val parameters = Map("nomeFuncao" -> "processar")
    val code = assistant.generateCode(template, parameters)

    assert(code.content.contains("def processar"))
