package com.rbleggi.validationframework;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@FunctionalInterface
interface Validator<T> {
    List<String> validate(T value);
}

record User(String name, String email) {}

class Validators {
    static final Validator<String> notNullString = value ->
        value != null ? List.of() : List.of("Value cannot be null.");

    static Validator<String> minLength(int min) {
        return value -> (value != null && value.length() >= min)
            ? List.of()
            : List.of("Value does not meet minimum length " + min + ".");
    }

    @SafeVarargs
    static <T> Validator<T> all(Validator<T>... validators) {
        return value -> Arrays.stream(validators)
            .flatMap(v -> v.validate(value).stream())
            .toList();
    }
}

class UserValidator implements Validator<User> {
    private final Validator<String> nameValidator = Validators.all(Validators.notNullString, Validators.minLength(3));
    private final Validator<String> emailValidator = Validators.notNullString;

    @Override
    public List<String> validate(User user) {
        List<String> errors = new ArrayList<>();
        nameValidator.validate(user.name()).forEach(e -> errors.add("name: " + e));
        emailValidator.validate(user.email()).forEach(e -> errors.add("email: " + e));
        return errors;
    }
}

public class Main {
    public static void main(String[] args) {
        System.out.println("=== Validation Framework Examples ===\n");

        User validUser = new User("John Doe", "john@example.com");
        User invalidUser = new User(null, "a@b.com");
        User shortNameUser = new User("Jo", "jo@example.com");

        UserValidator validator = new UserValidator();

        System.out.println("Valid user errors: " + validator.validate(validUser));
        System.out.println("Invalid user errors: " + validator.validate(invalidUser));
        System.out.println("Short name user errors: " + validator.validate(shortNameUser));
    }
}
