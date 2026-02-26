package com.rbleggi.yamlcodegenerator;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class YamlCodeGeneratorTest {

    @Test
    @DisplayName("DataClassGenerator should generate data class from YAML")
    void dataClassGenerator_validYaml_generatesDataClass() {
        var yaml = """
Person:
  name: String
  age: Int
""";
        var generator = new DataClassGenerator();
        var code = generator.generate(yaml);
        assertTrue(code.contains("data class Person"));
        assertTrue(code.contains("val name: String"));
        assertTrue(code.contains("val age: Int"));
    }

    @Test
    @DisplayName("DataClassGenerator should handle single field")
    void dataClassGenerator_singleField_generatesCorrectly() {
        var yaml = """
User:
  id: Long
""";
        var generator = new DataClassGenerator();
        var code = generator.generate(yaml);
        assertTrue(code.contains("data class User"));
        assertTrue(code.contains("val id: Long"));
    }

    @Test
    @DisplayName("DataClassGenerator should handle multiple fields")
    void dataClassGenerator_multipleFields_generatesCorrectly() {
        var yaml = """
Address:
  street: String
  number: Int
  city: String
""";
        var generator = new DataClassGenerator();
        var code = generator.generate(yaml);
        assertTrue(code.contains("data class Address"));
        assertTrue(code.contains("val street: String"));
        assertTrue(code.contains("val number: Int"));
        assertTrue(code.contains("val city: String"));
    }

    @Test
    @DisplayName("DataClassGenerator should handle complex types")
    void dataClassGenerator_complexTypes_generatesCorrectly() {
        var yaml = """
Person:
  name: String
  address: Address
  phones: List<String>
""";
        var generator = new DataClassGenerator();
        var code = generator.generate(yaml);
        assertTrue(code.contains("val address: Address"));
        assertTrue(code.contains("val phones: List<String>"));
    }

    @Test
    @DisplayName("DataClassGenerator should handle empty YAML")
    void dataClassGenerator_emptyYaml_returnsComment() {
        var yaml = "";
        var generator = new DataClassGenerator();
        var code = generator.generate(yaml);
        assertTrue(code.contains("empty YAML"));
    }

    @Test
    @DisplayName("DataClassGenerator should trim whitespace")
    void dataClassGenerator_withWhitespace_trimsCorrectly() {
        var yaml = """
  Person:
    name: String
    age: Int
""";
        var generator = new DataClassGenerator();
        var code = generator.generate(yaml);
        assertTrue(code.contains("data class Person"));
        assertTrue(code.contains("val name: String"));
    }

    @Test
    @DisplayName("GeneratorStrategy interface should be implemented by DataClassGenerator")
    void generatorStrategy_dataClassGenerator_implementsInterface() {
        GeneratorStrategy generator = new DataClassGenerator();
        assertNotNull(generator);
    }

    @Test
    @DisplayName("DataClassGenerator should format fields with commas")
    void dataClassGenerator_multipleFields_formatsWithCommas() {
        var yaml = """
Person:
  firstName: String
  lastName: String
""";
        var generator = new DataClassGenerator();
        var code = generator.generate(yaml);
        assertTrue(code.contains(","));
    }

    @Test
    @DisplayName("RecordGenerator should generate record from YAML")
    void recordGenerator_validYaml_generatesRecord() {
        var yaml = """
Person:
  name: String
  age: int
""";
        var generator = new RecordGenerator();
        var code = generator.generate(yaml);
        assertTrue(code.contains("record Person("));
        assertTrue(code.contains("String name"));
        assertTrue(code.contains("int age"));
    }

    @Test
    @DisplayName("RecordGenerator should handle single field")
    void recordGenerator_singleField_generatesCorrectly() {
        var yaml = """
UserId:
  value: long
""";
        var generator = new RecordGenerator();
        var code = generator.generate(yaml);
        assertEquals("record UserId(long value) {}", code);
    }

    @Test
    @DisplayName("RecordGenerator should handle multiple fields")
    void recordGenerator_multipleFields_generatesCorrectly() {
        var yaml = """
Address:
  street: String
  number: int
  city: String
""";
        var generator = new RecordGenerator();
        var code = generator.generate(yaml);
        assertTrue(code.contains("record Address("));
        assertTrue(code.contains("String street"));
        assertTrue(code.contains("int number"));
        assertTrue(code.contains("String city"));
    }

    @Test
    @DisplayName("RecordGenerator should handle empty YAML")
    void recordGenerator_emptyYaml_returnsComment() {
        var generator = new RecordGenerator();
        var code = generator.generate("");
        assertTrue(code.contains("empty YAML"));
    }

    @Test
    @DisplayName("RecordGenerator should handle complex types")
    void recordGenerator_complexTypes_generatesCorrectly() {
        var yaml = """
Order:
  id: long
  items: List<String>
  total: double
""";
        var generator = new RecordGenerator();
        var code = generator.generate(yaml);
        assertTrue(code.contains("record Order("));
        assertTrue(code.contains("List<String> items"));
    }

    @Test
    @DisplayName("RecordGenerator should implement GeneratorStrategy")
    void recordGenerator_implementsStrategy() {
        GeneratorStrategy generator = new RecordGenerator();
        assertNotNull(generator);
    }
}
