package com.rbleggi.validationframework;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@interface NotBlank {}

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@interface Email {}

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@interface Positive {}

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@interface Size {
    int min() default 0;
    int max() default Integer.MAX_VALUE;
}

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@interface Min {
    long value();
}

record ValidationResult(boolean valid, List<String> errors) {
    static ValidationResult of(List<String> errors) {
        return new ValidationResult(errors.isEmpty(), List.copyOf(errors));
    }
}

class Validator {

    private static final Pattern EMAIL = Pattern.compile("^[\\w.+-]+@[\\w.-]+\\.[a-zA-Z]{2,}$");

    static ValidationResult validate(Object target) {
        List<String> errors = new ArrayList<>();
        for (Field field : target.getClass().getDeclaredFields()) {
            field.setAccessible(true);
            Object value = readValue(field, target);
            String name = field.getName();

            if (field.isAnnotationPresent(NotBlank.class)
                    && (value == null || value.toString().isBlank())) {
                errors.add(name + " must not be blank");
            }
            if (field.isAnnotationPresent(Email.class)
                    && (value == null || !EMAIL.matcher(value.toString()).matches())) {
                errors.add(name + " must be a valid email");
            }
            if (field.isAnnotationPresent(Positive.class)
                    && (!(value instanceof Number n) || n.doubleValue() <= 0)) {
                errors.add(name + " must be positive");
            }
            if (field.isAnnotationPresent(Size.class)) {
                Size size = field.getAnnotation(Size.class);
                int length = value == null ? 0 : value.toString().length();
                if (length < size.min() || length > size.max()) {
                    errors.add(name + " length must be between " + size.min() + " and " + size.max());
                }
            }
            if (field.isAnnotationPresent(Min.class)) {
                Min min = field.getAnnotation(Min.class);
                if (!(value instanceof Number n) || n.longValue() < min.value()) {
                    errors.add(name + " must be at least " + min.value());
                }
            }
        }
        return ValidationResult.of(errors);
    }

    private static Object readValue(Field field, Object target) {
        try {
            return field.get(target);
        } catch (IllegalAccessException e) {
            throw new IllegalStateException("Cannot read field " + field.getName(), e);
        }
    }
}

class Product {
    @NotBlank @Size(min = 3, max = 100) String name;
    @NotBlank String category;
    @Positive double price;
    @Min(0) int stock;

    Product(String name, String category, double price, int stock) {
        this.name = name;
        this.category = category;
        this.price = price;
        this.stock = stock;
    }
}

class Order {
    @NotBlank @Size(min = 5) String orderId;
    @NotBlank @Email String customerEmail;
    @Min(0) int quantity;

    Order(String orderId, String customerEmail, int quantity) {
        this.orderId = orderId;
        this.customerEmail = customerEmail;
        this.quantity = quantity;
    }
}

public class Main {
    public static void main(String[] args) {
        System.out.println("Validation Framework");
    }
}
