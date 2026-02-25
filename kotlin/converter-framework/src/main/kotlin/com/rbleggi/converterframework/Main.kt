package com.rbleggi.converterframework

fun interface Converter<A, B> {
    fun convert(input: A): B
}

infix fun <A, B, C> Converter<A, B>.then(next: Converter<B, C>): Converter<A, C> {
    return Converter { input -> next.convert(this.convert(input)) }
}

data class Address(val street: String, val city: String, val zip: String)
data class Person(val name: String, val age: Int, val address: Address)
data class PersonDTO(val fullName: String, val age: Int, val city: String)

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
    println("Converter Framework")
}
