package com.rbleggi.grocerytodolist;

import java.util.ArrayList;
import java.util.List;

public class AddItemCommand implements Command {
    private final GroceryItem item;

    public AddItemCommand(GroceryItem item) {
        this.item = item;
    }

    @Override
    public List<GroceryItem> execute(List<GroceryItem> items) {
        if (items.stream().noneMatch(i -> i.getName().equals(item.getName()))) {
            System.out.println("Added: " + item.getName());
            List<GroceryItem> newItems = new ArrayList<>(items);
            newItems.add(item);
            return newItems;
        } else {
            System.out.println("Item already exists: " + item.getName());
            return items;
        }
    }

    @Override
    public List<GroceryItem> undo(List<GroceryItem> items) {
        int index = -1;
        for (int i = 0; i < items.size(); i++) {
            if (items.get(i).getName().equals(item.getName())) {
                index = i;
                break;
            }
        }
        if (index >= 0) {
            System.out.println("Undone addition: " + item.getName());
            List<GroceryItem> newItems = new ArrayList<>(items);
            newItems.remove(index);
            return newItems;
        } else {
            System.out.println("Item not found for undo: " + item.getName());
            return items;
        }
    }
}

