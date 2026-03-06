package com.rbleggi.multimodalaisystem

sealed class Input {
    data class TextInput(val text: String) : Input()
    data class NumericInput(val value: Double) : Input()
    data class CategoricalInput(val category: String) : Input()
}

data class ProcessingResult(val input: Input, val output: String, val mode: String)

interface ProcessingStrategy {
    fun process(input: Input): ProcessingResult
}

class TextProcessingStrategy : ProcessingStrategy {
    override fun process(input: Input): ProcessingResult {
        return when (input) {
            is Input.TextInput -> {
                val wordCount = input.text.split(" ").size
                val charCount = input.text.length
                ProcessingResult(
                    input,
                    "Texto analisado: $wordCount palavras, $charCount caracteres",
                    "text"
                )
            }
            else -> ProcessingResult(input, "Entrada invalida para modo texto", "text")
        }
    }
}

class NumericProcessingStrategy : ProcessingStrategy {
    override fun process(input: Input): ProcessingResult {
        return when (input) {
            is Input.NumericInput -> {
                val squared = input.value * input.value
                val sqrt = kotlin.math.sqrt(input.value)
                ProcessingResult(
                    input,
                    "Numero processado: quadrado = %.2f, raiz = %.2f".format(squared, sqrt),
                    "numeric"
                )
            }
            else -> ProcessingResult(input, "Entrada invalida para modo numerico", "numeric")
        }
    }
}

class CategoricalProcessingStrategy : ProcessingStrategy {
    private val categoryMapping = mapOf(
        "produto" to "Categoria: Itens Fisicos",
        "servico" to "Categoria: Servicos",
        "digital" to "Categoria: Produtos Digitais"
    )

    override fun process(input: Input): ProcessingResult {
        return when (input) {
            is Input.CategoricalInput -> {
                val mapping = categoryMapping[input.category.lowercase()]
                    ?: "Categoria desconhecida: ${input.category}"
                ProcessingResult(input, mapping, "categorical")
            }
            else -> ProcessingResult(input, "Entrada invalida para modo categorico", "categorical")
        }
    }
}

class MultiModalAISystem(private val strategy: ProcessingStrategy) {
    fun process(input: Input): ProcessingResult {
        return strategy.process(input)
    }

    fun processBatch(inputs: List<Input>): List<ProcessingResult> {
        return inputs.map { process(it) }
    }
}

fun main() {
    println("Multi-Modal AI System")
}
