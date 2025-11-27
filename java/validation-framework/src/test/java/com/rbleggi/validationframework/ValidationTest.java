package com.rbleggi.validationframework;

import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class ValidationTest {
    @Test
    void testNotNullStringValidator() {
        assertTrue(Validators.notNullString.validate("test").isEmpty());
        assertEquals(1, Validators.notNullString.validate(null).size());
        assertEquals("Value cannot be null.", Validators.notNullString.validate(null).get(0));
    }

    @Test
    void testMinLengthValidator() {
        Validator<String> minLength3 = Validators.minLength(3);
        assertTrue(minLength3.validate("abc").isEmpty());
        assertTrue(minLength3.validate("abcd").isEmpty());
        assertEquals(1, minLength3.validate("ab").size());
        assertEquals(1, minLength3.validate(null).size());
    }

    @Test
    void testAllValidator() {
        Validator<String> combined = Validators.all(
            Validators.notNullString,
            Validators.minLength(3)
        );
        assertTrue(combined.validate("abc").isEmpty());
        assertEquals(1, combined.validate("ab").size());
        assertEquals(2, combined.validate(null).size());
    }

    @Test
    void testUserValidatorWithValidUser() {
        User user = new User("John Doe", "john@example.com");
        UserValidator validator = new UserValidator();
        List<String> errors = validator.validate(user);
        assertTrue(errors.isEmpty());
    }

    @Test
    void testUserValidatorWithNullName() {
        User user = new User(null, "test@example.com");
        UserValidator validator = new UserValidator();
        List<String> errors = validator.validate(user);
        assertEquals(2, errors.size());
        assertTrue(errors.stream().anyMatch(e -> e.contains("name: Value cannot be null")));
    }

    @Test
    void testUserValidatorWithShortName() {
        User user = new User("Jo", "test@example.com");
        UserValidator validator = new UserValidator();
        List<String> errors = validator.validate(user);
        assertEquals(1, errors.size());
        assertTrue(errors.get(0).contains("minimum length 3"));
    }

    @Test
    void testUserValidatorWithNullEmail() {
        User user = new User("John Doe", null);
        UserValidator validator = new UserValidator();
        List<String> errors = validator.validate(user);
        assertEquals(1, errors.size());
        assertTrue(errors.get(0).contains("email: Value cannot be null"));
    }
}
