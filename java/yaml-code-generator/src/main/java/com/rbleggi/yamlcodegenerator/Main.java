package com.rbleggi.yamlcodegenerator;

import java.util.Arrays;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) {
        var yamlContent = """
Person:
  name: String
  age: Int
  address: Address
  phones: List<String>

Address:
  street: String
  number: Int
  city: String
  state: String

Company:
  name: String
  cnpj: String
  employees: List<Person>
""";

        GeneratorStrategy generator = new DataClassGenerator();
        var generatedCode = generator.generate(yamlContent);

        System.out.println("Generated code:\n" + generatedCode);
    }
}

interface GeneratorStrategy {
    String generate(String yaml);
}

class DataClassGenerator implements GeneratorStrategy {
    @Override
    public String generate(String yaml) {
        var lines = Arrays.stream(yaml.split("\n"))
            .map(String::trim)
            .filter(line -> !line.isEmpty())
            .toList();

        if (lines.isEmpty()) return "// empty YAML";

        var className = lines.get(0).replace(":", "");
        var fields = lines.stream()
            .skip(1)
            .map(line -> {
                var parts = Arrays.stream(line.split(":"))
                    .map(String::trim)
                    .toArray(String[]::new);
                return "  val " + parts[0] + ": " + parts[1];
            })
            .collect(Collectors.joining(",\n"));

        return "data class " + className + "(\n" + fields + "\n)";
    }
}
