package com.rbleggi.validationframework

interface Validator<T> {
    fun validate(value: T): List<String>
}

data class User(val name: String?, val email: String?)

object Validators {
    val notNullString: Validator<String?> = object : Validator<String?> {
        override fun validate(value: String?): List<String> =
            if (value != null) emptyList() else listOf("Value cannot be null.")
    }

    fun minLength(min: Int): Validator<String?> = object : Validator<String?> {
        override fun validate(value: String?): List<String> =
            if (value != null && value.length >= min) emptyList() else listOf("Value does not meet minimum length $min.")
    }

    fun <T> all(vararg validators: Validator<T>): Validator<T> = object : Validator<T> {
        override fun validate(value: T): List<String> =
            validators.flatMap { it.validate(value) }
    }
}

class UserValidator : Validator<User> {
    private val nameValidator = Validators.all(Validators.notNullString, Validators.minLength(3))
    private val emailValidator = Validators.notNullString

    override fun validate(user: User): List<String> =
        nameValidator.validate(user.name).map { "name: $it" } +
        emailValidator.validate(user.email).map { "email: $it" }
}

fun main() {
    val user = User(null, "a@b.com")
    val validator = UserValidator()
    val errors = validator.validate(user)
    println(errors)
}
