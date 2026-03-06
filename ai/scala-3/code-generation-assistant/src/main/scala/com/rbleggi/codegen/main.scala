package com.rbleggi.codegen

case class Template(name: String, content: String)

case class GeneratedCode(file: String, content: String)

trait GeneratorStrategy:
  def generate(template: Template, parameters: Map[String, String]): GeneratedCode

class ClassGenerator extends GeneratorStrategy:
  def generate(template: Template, parameters: Map[String, String]): GeneratedCode =
    var code = template.content
    parameters.foreach { case (key, value) =>
      code = code.replace(s"{{$key}}", value)
    }
    val fileName = parameters.getOrElse("nomeClasse", "MinhaClasse") + ".scala"
    GeneratedCode(fileName, code)

class FunctionGenerator extends GeneratorStrategy:
  def generate(template: Template, parameters: Map[String, String]): GeneratedCode =
    var code = template.content
    parameters.foreach { case (key, value) =>
      code = code.replace(s"{{$key}}", value)
    }
    val fileName = parameters.getOrElse("nomeFuncao", "minhaFuncao") + ".scala"
    GeneratedCode(fileName, code)

class TestGenerator extends GeneratorStrategy:
  def generate(template: Template, parameters: Map[String, String]): GeneratedCode =
    var code = template.content
    parameters.foreach { case (key, value) =>
      code = code.replace(s"{{$key}}", value)
    }
    val fileName = parameters.getOrElse("nomeClasse", "MinhaClasse") + "Spec.scala"
    GeneratedCode(fileName, code)

class CodeAssistant(generator: GeneratorStrategy):
  def generateCode(template: Template, parameters: Map[String, String]): GeneratedCode =
    generator.generate(template, parameters)

@main def run(): Unit =
  println("Code Generation Assistant")
