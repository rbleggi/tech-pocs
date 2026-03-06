package com.rbleggi.codegenerationassistant;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class CodeGenerationTest {

    @Test
    @DisplayName("ClassGeneratorStrategy - gera classe com campos e construtor")
    void classGenerator_generatesClassWithFields() {
        CodeRequest request = new CodeRequest(
            "Cliente",
            "class",
            List.of("nome:String", "cpf:String"),
            Map.of()
        );

        ClassGeneratorStrategy strategy = new ClassGeneratorStrategy();
        GeneratedCode code = strategy.generate(request);

        assertTrue(code.code().contains("public class Cliente"));
        assertTrue(code.code().contains("private String nome"));
        assertTrue(code.code().contains("public Cliente(String nome, String cpf)"));
        assertTrue(code.code().contains("public String getNome()"));
        assertEquals("Java", code.language());
        assertEquals("class", code.type());
    }

    @Test
    @DisplayName("FunctionGeneratorStrategy - gera funcao com parametros e retorno")
    void functionGenerator_generatesFunctionWithReturn() {
        CodeRequest request = new CodeRequest(
            "calcularTotal",
            "function",
            List.of("preco:double", "quantidade:int"),
            Map.of("returnType", "double")
        );

        FunctionGeneratorStrategy strategy = new FunctionGeneratorStrategy();
        GeneratedCode code = strategy.generate(request);

        assertTrue(code.code().contains("public double calcularTotal"));
        assertTrue(code.code().contains("double preco, int quantidade"));
        assertTrue(code.code().contains("return 0"));
        assertEquals("function", code.type());
    }

    @Test
    @DisplayName("FunctionGeneratorStrategy - gera funcao void")
    void functionGenerator_generatesVoidFunction() {
        CodeRequest request = new CodeRequest(
            "imprimirMensagem",
            "function",
            List.of("mensagem:String"),
            Map.of("returnType", "void")
        );

        FunctionGeneratorStrategy strategy = new FunctionGeneratorStrategy();
        GeneratedCode code = strategy.generate(request);

        assertTrue(code.code().contains("public void imprimirMensagem"));
        assertFalse(code.code().contains("return"));
    }

    @Test
    @DisplayName("TestGeneratorStrategy - gera classe de teste com metodos")
    void testGenerator_generatesTestClass() {
        CodeRequest request = new CodeRequest(
            "ProdutoTest",
            "test",
            List.of("calcularPreco", "aplicarDesconto"),
            Map.of("targetClass", "Produto")
        );

        TestGeneratorStrategy strategy = new TestGeneratorStrategy();
        GeneratedCode code = strategy.generate(request);

        assertTrue(code.code().contains("class ProdutoTest"));
        assertTrue(code.code().contains("private Produto target"));
        assertTrue(code.code().contains("@BeforeEach"));
        assertTrue(code.code().contains("@Test"));
        assertTrue(code.code().contains("calcularPreco_deveRetornarResultadoEsperado"));
        assertEquals("test", code.type());
    }

    @Test
    @DisplayName("CodeGenerationAssistant - processa requisicao unica")
    void assistant_processesSingleRequest() {
        CodeRequest request = new CodeRequest(
            "Venda",
            "class",
            List.of("valor:double"),
            Map.of()
        );

        CodeGenerationAssistant assistant = new CodeGenerationAssistant(new ClassGeneratorStrategy());
        GeneratedCode code = assistant.generateCode(request);

        assertNotNull(code);
        assertTrue(code.code().contains("Venda"));
    }

    @Test
    @DisplayName("CodeGenerationAssistant - processa lote de requisicoes")
    void assistant_processesBatchRequests() {
        List<CodeRequest> requests = List.of(
            new CodeRequest("A", "class", List.of("x:int"), Map.of()),
            new CodeRequest("B", "class", List.of("y:String"), Map.of())
        );

        CodeGenerationAssistant assistant = new CodeGenerationAssistant(new ClassGeneratorStrategy());
        List<GeneratedCode> results = assistant.generateBatch(requests);

        assertEquals(2, results.size());
        assertTrue(results.get(0).code().contains("class A"));
        assertTrue(results.get(1).code().contains("class B"));
    }
}
