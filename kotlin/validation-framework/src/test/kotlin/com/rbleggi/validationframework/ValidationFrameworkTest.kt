package com.rbleggi.validationframework

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class ValidationFrameworkTest {

    @Test
    fun `notNullString should pass for non-null values`() {
        val validator = Validators.notNullString
        val errors = validator.validate("test")
        assertTrue(errors.isEmpty())
    }

    @Test
    fun `notNullString should fail for null values`() {
        val validator = Validators.notNullString
        val errors = validator.validate(null)
        assertEquals(1, errors.size)
        assertEquals("Value cannot be null.", errors[0])
    }

    @Test
    fun `minLength should pass for strings meeting minimum length`() {
        val validator = Validators.minLength(3)
        val errors = validator.validate("abc")
        assertTrue(errors.isEmpty())
    }

    @Test
    fun `minLength should fail for strings below minimum length`() {
        val validator = Validators.minLength(3)
        val errors = validator.validate("ab")
        assertEquals(1, errors.size)
        assertEquals("Value does not meet minimum length 3.", errors[0])
    }

    @Test
    fun `minLength should fail for null values`() {
        val validator = Validators.minLength(3)
        val errors = validator.validate(null)
        assertEquals(1, errors.size)
    }

    @Test
    fun `all should combine multiple validators`() {
        val validator = Validators.all(Validators.notNullString, Validators.minLength(5))
        val errors = validator.validate("abc")
        assertEquals(1, errors.size)
        assertEquals("Value does not meet minimum length 5.", errors[0])
    }

    @Test
    fun `all should return all errors from all validators`() {
        val validator = Validators.all(Validators.notNullString, Validators.minLength(5))
        val errors = validator.validate(null)
        assertEquals(2, errors.size)
    }

    @Test
    fun `UserValidator should validate user name`() {
        val validator = UserValidator()
        val user = User(null, "test@test.com")
        val errors = validator.validate(user)
        assertTrue(errors.any { it.startsWith("name:") })
    }

    @Test
    fun `UserValidator should validate user email`() {
        val validator = UserValidator()
        val user = User("John", null)
        val errors = validator.validate(user)
        assertTrue(errors.any { it.startsWith("email:") })
    }

    @Test
    fun `UserValidator should pass for valid user`() {
        val validator = UserValidator()
        val user = User("John", "john@test.com")
        val errors = validator.validate(user)
        assertTrue(errors.isEmpty())
    }

    @Test
    fun `UserValidator should fail for short name`() {
        val validator = UserValidator()
        val user = User("Jo", "john@test.com")
        val errors = validator.validate(user)
        assertEquals(1, errors.size)
        assertTrue(errors[0].startsWith("name:"))
    }

    @Test
    fun `UserValidator should return multiple errors for invalid user`() {
        val validator = UserValidator()
        val user = User(null, null)
        val errors = validator.validate(user)
        assertTrue(errors.size >= 2)
    }
}
