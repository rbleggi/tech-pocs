package com.rbleggi.yamlcodegenerator

@main def runYamlCodeGenerator(): Unit =
  println("YAML Code Generator")

trait GeneratorStrategy {
  def generate(yaml: String): String
}

class CaseClassGenerator extends GeneratorStrategy {
  override def generate(yaml: String): String = {
    val lines = yaml.split("\n").map(_.trim).filter(_.nonEmpty)
    if (lines.isEmpty) return "// empty YAML"

    val className = lines.head.replace(":", "")
    val fields = lines.tail.map { line =>
      val Array(name, tpe) = line.split(":").map(_.trim)
      s"  $name: $tpe"
    }
    s"case class $className(\n${fields.mkString(",\n")}\n)"
  }
}
