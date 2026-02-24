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
        System.out.println("Converter Framework");
    }
}
