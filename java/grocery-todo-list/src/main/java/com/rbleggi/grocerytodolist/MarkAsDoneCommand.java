package com.rbleggi.grocerytodolist;

import java.util.ArrayList;
import java.util.List;

public class MarkAsDoneCommand implements Command {
    private final GroceryItem item;
    private GroceryItem previousState;

    public MarkAsDoneCommand(GroceryItem item) {
        this.item = item;
    }

    @Override
    public List<GroceryItem> execute(List<GroceryItem> items) {
        int index = -1;
        for (int i = 0; i < items.size(); i++) {
            if (items.get(i).getName().equals(item.getName())) {
                index = i;
                break;
            }
        }
        if (index >= 0) {
            previousState = items.get(index);
            GroceryItem updatedItem = items.get(index).markAsDone();
            System.out.println("Marked as done: " + item.getName());
            List<GroceryItem> newItems = new ArrayList<>(items);
            newItems.set(index, updatedItem);
            return newItems;
        } else {
            System.out.println("Item not found: " + item.getName());
            return items;
        }
    }

    @Override
    public List<GroceryItem> undo(List<GroceryItem> items) {
        if (previousState != null) {
            int index = -1;
            for (int i = 0; i < items.size(); i++) {
                if (items.get(i).getName().equals(item.getName())) {
                    index = i;
                    break;
                }
            }
            if (index >= 0) {
                System.out.println("Undone mark as done: " + item.getName());
                List<GroceryItem> newItems = new ArrayList<>(items);
                newItems.set(index, previousState);
                return newItems;
            } else {
                System.out.println("Item not found for undo: " + item.getName());
                return items;
            }
        } else {
            System.out.println("Cannot undo: " + item.getName());
            return items;
        }
    }
}

