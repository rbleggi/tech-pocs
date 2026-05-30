package com.rbleggi.validationframework;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ValidationTest {

    @Test
    @DisplayName("a fully valid product passes validation")
    void validProduct_passes() {
        ValidationResult result = Validator.validate(new Product("Cafe Especial", "Bebidas", 29.90, 100));
        assertTrue(result.valid());
        assertTrue(result.errors().isEmpty());
    }

    @Test
    @DisplayName("@NotBlank reports a blank name")
    void blankName_fails() {
        ValidationResult result = Validator.validate(new Product("", "Bebidas", 29.90, 10));
        assertFalse(result.valid());
        assertTrue(result.errors().stream().anyMatch(e -> e.contains("name") && e.contains("blank")));
    }

    @Test
    @DisplayName("@Size reports a name shorter than the minimum")
    void shortName_fails() {
        ValidationResult result = Validator.validate(new Product("AB", "Bebidas", 29.90, 10));
        assertFalse(result.valid());
        assertTrue(result.errors().stream().anyMatch(e -> e.contains("name") && e.contains("length")));
    }

    @Test
    @DisplayName("@Size reports a name longer than the maximum")
    void longName_fails() {
        String huge = "x".repeat(101);
        ValidationResult result = Validator.validate(new Product(huge, "Bebidas", 29.90, 10));
        assertFalse(result.valid());
        assertTrue(result.errors().stream().anyMatch(e -> e.contains("name") && e.contains("length")));
    }

    @Test
    @DisplayName("@Positive reports a zero or negative price")
    void nonPositivePrice_fails() {
        assertFalse(Validator.validate(new Product("Cafe Especial", "Bebidas", 0.0, 10)).valid());
        ValidationResult result = Validator.validate(new Product("Cafe Especial", "Bebidas", -1.0, 10));
        assertFalse(result.valid());
        assertTrue(result.errors().stream().anyMatch(e -> e.contains("price") && e.contains("positive")));
    }

    @Test
    @DisplayName("@Positive accepts a fractional positive price")
    void fractionalPositivePrice_passes() {
        assertTrue(Validator.validate(new Product("Cafe Especial", "Bebidas", 0.99, 10)).valid());
    }

    @Test
    @DisplayName("@Min(0) reports a negative stock")
    void negativeStock_fails() {
        ValidationResult result = Validator.validate(new Product("Cafe Especial", "Bebidas", 29.90, -5));
        assertFalse(result.valid());
        assertTrue(result.errors().stream().anyMatch(e -> e.contains("stock") && e.contains("at least")));
    }

    @Test
    @DisplayName("a product with several invalid fields accumulates all errors")
    void multipleInvalidProductFields_returnsAllErrors() {
        ValidationResult result = Validator.validate(new Product("", "", -1.0, -1));
        assertFalse(result.valid());
        assertTrue(result.errors().size() >= 4);
    }

    @Test
    @DisplayName("a fully valid order passes validation")
    void validOrder_passes() {
        ValidationResult result = Validator.validate(new Order("ORD-001", "joao@example.com.br", 5));
        assertTrue(result.valid());
        assertTrue(result.errors().isEmpty());
    }

    @Test
    @DisplayName("@Email reports an invalid customer email")
    void invalidEmail_fails() {
        ValidationResult result = Validator.validate(new Order("ORD-002", "not-an-email", 2));
        assertFalse(result.valid());
        assertTrue(result.errors().stream().anyMatch(e -> e.contains("customerEmail") && e.contains("email")));
    }

    @Test
    @DisplayName("@Size reports an orderId shorter than the minimum")
    void shortOrderId_fails() {
        ValidationResult result = Validator.validate(new Order("ORD", "maria@example.com", 1));
        assertFalse(result.valid());
        assertTrue(result.errors().stream().anyMatch(e -> e.contains("orderId") && e.contains("length")));
    }

    @Test
    @DisplayName("@Min(0) reports a negative quantity")
    void negativeQuantity_fails() {
        ValidationResult result = Validator.validate(new Order("ORD-003", "maria@example.com", -1));
        assertFalse(result.valid());
        assertTrue(result.errors().stream().anyMatch(e -> e.contains("quantity") && e.contains("at least")));
    }

    @Test
    @DisplayName("an order with several invalid fields accumulates all errors")
    void multipleInvalidOrderFields_returnsAllErrors() {
        ValidationResult result = Validator.validate(new Order("", "bad-email", -10));
        assertFalse(result.valid());
        assertTrue(result.errors().size() >= 3);
    }
}
