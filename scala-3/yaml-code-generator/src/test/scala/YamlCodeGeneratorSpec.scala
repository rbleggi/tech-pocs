package com.rbleggi.yamlcodegenerator

import org.scalatest.funsuite.AnyFunSuite

class YamlCodeGeneratorSpec extends AnyFunSuite {
  test("YamlCodeGenerator should generate valid YAML for a simple map") {
    val generator = new YamlCodeGenerator
    val data = Map("name" -> "Scala", "type" -> "language")
    val yaml = generator.generate(data)
    assert(yaml.contains("name: Scala"))
    assert(yaml.contains("type: language"))
  }
}
