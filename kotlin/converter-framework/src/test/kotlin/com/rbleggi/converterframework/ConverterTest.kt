package com.rbleggi.converterframework

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*

class ConverterTest {

    @Test
    fun `test address to string converter`() {
        val address = Address("123 Main St", "Springfield", "12345")
        val result = addressToString.convert(address)
        assertEquals("123 Main St, Springfield, 12345", result)
    }

    @Test
    fun `test person to DTO converter`() {
        val address = Address("123 Main St", "Springfield", "12345")
        val person = Person("John Doe", 30, address)
        val result = personToDTO.convert(person)

        assertEquals("John Doe", result.fullName)
        assertEquals(30, result.age)
        assertEquals("Springfield", result.city)
    }

    @Test
    fun `test person to string converter`() {
        val address = Address("123 Main St", "Springfield", "12345")
        val person = Person("John Doe", 30, address)
        val result = personToString.convert(person)

        assertEquals("John Doe (30), 123 Main St, Springfield, 12345", result)
    }

    @Test
    fun `test custom converter`() {
        val intToString: Converter<Int, String> = Converter { it.toString() }
        val result = intToString.convert(42)
        assertEquals("42", result)
    }

    @Test
    fun `test converter composition with then`() {
        val intToDouble: Converter<Int, Double> = Converter { it.toDouble() }
        val doubleToString: Converter<Double, String> = Converter { "Value: $it" }

        val composed = intToDouble then doubleToString
        val result = composed.convert(42)

        assertEquals("Value: 42.0", result)
    }

    @Test
    fun `test converter composition chain`() {
        val address = Address("456 Oak Ave", "Portland", "54321")
        val person = Person("Jane Smith", 25, address)

        val personToDTOThenToString: Converter<Person, String> = personToDTO then Converter { dto ->
            "${dto.fullName} lives in ${dto.city}"
        }

        val result = personToDTOThenToString.convert(person)
        assertEquals("Jane Smith lives in Portland", result)
    }

    @Test
    fun `test multiple converter compositions`() {
        val step1: Converter<Int, Int> = Converter { it * 2 }
        val step2: Converter<Int, Int> = Converter { it + 10 }
        val step3: Converter<Int, String> = Converter { "Result: $it" }

        val composed = step1 then step2 then step3
        val result = composed.convert(5)

        assertEquals("Result: 20", result)
    }

    @Test
    fun `test address with different values`() {
        val address = Address("789 Pine Rd", "Seattle", "98101")
        val result = addressToString.convert(address)
        assertEquals("789 Pine Rd, Seattle, 98101", result)
    }

    @Test
    fun `test person with different age`() {
        val address = Address("321 Elm St", "Austin", "73301")
        val person = Person("Bob Johnson", 45, address)
        val dto = personToDTO.convert(person)

        assertEquals(45, dto.age)
        assertEquals("Austin", dto.city)
    }
}
