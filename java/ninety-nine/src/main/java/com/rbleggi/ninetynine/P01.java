package com.rbleggi.ninetynine;

import java.util.List;
import java.util.NoSuchElementException;

public class P01 {
    public static <A> A last(List<A> items) {
        System.out.println("Recursion step: " + items);
        if (items.isEmpty()) {
            throw new NoSuchElementException("last of empty list");
        }
        if (items.size() == 1) {
            return items.get(0);
        }
        return last(items.subList(1, items.size()));
    }

    public static void main(String[] args) {
        System.out.println("Find the last element of a list.");
        var numbers = List.of(1, 1, 2, 3, 5, 8);
        System.out.println("The list is: " + numbers);
        var lastElement = last(numbers);
        System.out.println("The last element is: " + lastElement);
    }
}
