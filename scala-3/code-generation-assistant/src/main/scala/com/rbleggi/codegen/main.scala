package com.rbleggi.codegen

case class Template(nome: String, conteudo: String)

case class CodigoGerado(arquivo: String, conteudo: String)

trait GeradorStrategy:
  def gerar(template: Template, parametros: Map[String, String]): CodigoGerado

class GeradorClasse extends GeradorStrategy:
  def gerar(template: Template, parametros: Map[String, String]): CodigoGerado =
    var codigo = template.conteudo
    parametros.foreach { case (chave, valor) =>
      codigo = codigo.replace(s"{{$chave}}", valor)
    }
    val nomeArquivo = parametros.getOrElse("nomeClasse", "MinhaClasse") + ".scala"
    CodigoGerado(nomeArquivo, codigo)

class GeradorFuncao extends GeradorStrategy:
  def gerar(template: Template, parametros: Map[String, String]): CodigoGerado =
    var codigo = template.conteudo
    parametros.foreach { case (chave, valor) =>
      codigo = codigo.replace(s"{{$chave}}", valor)
    }
    val nomeArquivo = parametros.getOrElse("nomeFuncao", "minhaFuncao") + ".scala"
    CodigoGerado(nomeArquivo, codigo)

class GeradorTeste extends GeradorStrategy:
  def gerar(template: Template, parametros: Map[String, String]): CodigoGerado =
    var codigo = template.conteudo
    parametros.foreach { case (chave, valor) =>
      codigo = codigo.replace(s"{{$chave}}", valor)
    }
    val nomeArquivo = parametros.getOrElse("nomeClasse", "MinhaClasse") + "Spec.scala"
    CodigoGerado(nomeArquivo, codigo)

class AssistenteGeracao(gerador: GeradorStrategy):
  def gerarCodigo(template: Template, parametros: Map[String, String]): CodigoGerado =
    gerador.gerar(template, parametros)

@main def run(): Unit =
  val templateClasse = Template(
    "ClassePessoa",
    """case class {{nomeClasse}}(nome: String, cpf: String, cidade: String)
      |
      |object {{nomeClasse}}:
      |  def criar(nome: String, cpf: String, cidade: String): {{nomeClasse}} =
      |    {{nomeClasse}}(nome, cpf, cidade)
      |""".stripMargin
  )

  val geradorClasse = GeradorClasse()
  val assistente = AssistenteGeracao(geradorClasse)

  val parametros = Map(
    "nomeClasse" -> "Pessoa"
  )

  val codigo = assistente.gerarCodigo(templateClasse, parametros)
  println(s"Arquivo: ${codigo.arquivo}")
  println(codigo.conteudo)
