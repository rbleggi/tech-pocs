package com.rbleggi.converterframework;

import java.util.function.Function;

@FunctionalInterface
interface Converter<A, B> {
    B convert(A input);

    default <C> Converter<A, C> then(Converter<B, C> next) {
        return input -> next.convert(this.convert(input));
    }
}

record Address(String street, String city, String zip) {}
record Person(String name, int age, Address address) {}
record PersonDTO(String fullName, int age, String city) {}

public class Main {
    static final Converter<Address, String> addressToString = addr ->
        String.format("%s, %s, %s", addr.street(), addr.city(), addr.zip());

    static final Converter<Person, PersonDTO> personToDTO = person ->
        new PersonDTO(person.name(), person.age(), person.address().city());

    static final Converter<Person, String> personToString = person ->
        String.format("%s (%d), %s", person.name(), person.age(),
            addressToString.convert(person.address()));

    public static void main(String[] args) {
        Address address = new Address("123 Main St", "Springfield", "12345");
        Person person = new Person("John Doe", 30, address);

        System.out.println("=== Converter Framework Examples ===\n");

        System.out.println("Address as String: " + addressToString.convert(address));
        System.out.println("Person as DTO: " + personToDTO.convert(person));
        System.out.println("Person as String: " + personToString.convert(person));

        System.out.println("\n=== Converter Composition ===\n");

        Converter<Person, String> personToDTOThenToString = personToDTO.then(dto ->
            String.format("DTO[fullName=%s, age=%d, city=%s]",
                dto.fullName(), dto.age(), dto.city()));

        System.out.println("Person to DTO to String: " + personToDTOThenToString.convert(person));
    }
}
