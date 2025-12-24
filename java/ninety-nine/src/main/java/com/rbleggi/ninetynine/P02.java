package com.rbleggi.ninetynine;

import java.util.List;
import java.util.NoSuchElementException;

public class P02 {
    public static <A> A penultimate(List<A> items) {
        System.out.println("Recursion step: " + items);
        if (items.size() < 2) {
            throw new NoSuchElementException("penultimate of list with less than two elements");
        }
        if (items.size() == 2) {
            return items.get(0);
        }
        return penultimate(items.subList(1, items.size()));
    }

    public static void main(String[] args) {
        System.out.println("Find the last but one element of a list.");
        var numbers = List.of(1, 1, 2, 3, 5, 8);
        System.out.println("The list is: " + numbers);
        var penultimateElement = penultimate(numbers);
        System.out.println("The penultimate element is: " + penultimateElement);
    }
}
