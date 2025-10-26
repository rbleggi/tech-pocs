package com.rbleggi.yamlcodegenerator

class YamlCodeGeneratorSpec {
  test("YamlCodeGenerator should generate valid YAML for a simple map") {
    val generator = new YamlCodeGenerator
    val data = Map("name" -> "Scala", "type" -> "language")
    val yaml = generator.generate(data)
    assert(yaml.contains("name: Scala"))
    assert(yaml.contains("type: language"))
  }
}
