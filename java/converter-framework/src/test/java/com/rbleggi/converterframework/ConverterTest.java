package com.rbleggi.converterframework;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ConverterTest {
    @Test
    void testAddressToString() {
        Address address = new Address("123 Main St", "Springfield", "12345");
        String result = Main.addressToString.convert(address);
        assertEquals("123 Main St, Springfield, 12345", result);
    }

    @Test
    void testPersonToDTO() {
        Address address = new Address("123 Main St", "Springfield", "12345");
        Person person = new Person("John Doe", 30, address);
        PersonDTO dto = Main.personToDTO.convert(person);
        assertEquals("John Doe", dto.fullName());
        assertEquals(30, dto.age());
        assertEquals("Springfield", dto.city());
    }

    @Test
    void testPersonToString() {
        Address address = new Address("123 Main St", "Springfield", "12345");
        Person person = new Person("John Doe", 30, address);
        String result = Main.personToString.convert(person);
        assertEquals("John Doe (30), 123 Main St, Springfield, 12345", result);
    }

    @Test
    void testConverterComposition() {
        Address address = new Address("456 Elm St", "Metropolis", "67890");
        Person person = new Person("Jane Smith", 25, address);
        Converter<Person, String> composed = Main.personToDTO.then(dto ->
            String.format("DTO[fullName=%s, age=%d, city=%s]",
                dto.fullName(), dto.age(), dto.city()));
        String result = composed.convert(person);
        assertEquals("DTO[fullName=Jane Smith, age=25, city=Metropolis]", result);
    }

    @Test
    void testMultipleCompositions() {
        Converter<Integer, String> intToString = i -> String.valueOf(i);
        Converter<String, Integer> stringLength = s -> s.length();
        Converter<Integer, Boolean> isEven = i -> i % 2 == 0;

        Converter<Integer, Boolean> composed = intToString.then(stringLength).then(isEven);
        assertTrue(composed.convert(42));
        assertFalse(composed.convert(123));
    }
}
