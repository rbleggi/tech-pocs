package com.rbleggi.grocerytodolist;

import java.util.ArrayList;
import java.util.List;

public class GroceryManager {
    private List<GroceryItem> items = new ArrayList<>();

    public List<GroceryItem> getItems() {
        return new ArrayList<>(items);
    }

    public void applyChanges(List<GroceryItem> newItems) {
        items = new ArrayList<>(newItems);
    }

    public void listAll() {
        System.out.println("\nGrocery List:");
        if (items.isEmpty()) {
            System.out.println("No items in the list.");
        } else {
            items.forEach(System.out::println);
        }
    }
}

