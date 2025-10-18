package com.rbleggi.converterframework

/**
 * Generic type-safe converter interface
 */
fun interface Converter<A, B> {
    fun convert(input: A): B
}

/**
 * Extension function to compose converters
 */
infix fun <A, B, C> Converter<A, B>.then(next: Converter<B, C>): Converter<A, C> {
    return Converter { input -> next.convert(this.convert(input)) }
}

// Domain models
data class Address(val street: String, val city: String, val zip: String)
data class Person(val name: String, val age: Int, val address: Address)
data class PersonDTO(val fullName: String, val age: Int, val city: String)

// Converters
val addressToString: Converter<Address, String> = Converter { addr ->
    "${addr.street}, ${addr.city}, ${addr.zip}"
}

val personToDTO: Converter<Person, PersonDTO> = Converter { person ->
    PersonDTO(person.name, person.age, person.address.city)
}

val personToString: Converter<Person, String> = Converter { person ->
    "${person.name} (${person.age}), ${addressToString.convert(person.address)}"
}

fun main() {
    val address = Address("123 Main St", "Springfield", "12345")
    val person = Person("John Doe", 30, address)

    println("=== Converter Framework Examples ===\n")

    println("Address as String: ${addressToString.convert(address)}")
    println("Person as DTO: ${personToDTO.convert(person)}")
    println("Person as String: ${personToString.convert(person)}")

    // Demonstrate composition
    println("\n=== Converter Composition ===\n")

    val personToDTOThenToString: Converter<Person, String> = personToDTO then Converter { dto ->
        "DTO[fullName=${dto.fullName}, age=${dto.age}, city=${dto.city}]"
    }

    println("Person to DTO to String: ${personToDTOThenToString.convert(person)}")
}
