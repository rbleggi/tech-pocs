package com.rbleggi.grocerytodolist;

import java.util.ArrayList;
import java.util.List;

public class RemoveItemCommand implements Command {
    private final GroceryItem item;
    private GroceryItem removedItem;

    public RemoveItemCommand(GroceryItem item) {
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
            removedItem = items.get(index);
            System.out.println("Removed: " + item.getName());
            List<GroceryItem> newItems = new ArrayList<>(items);
            newItems.remove(index);
            return newItems;
        } else {
            System.out.println("Item not found: " + item.getName());
            return items;
        }
    }

    @Override
    public List<GroceryItem> undo(List<GroceryItem> items) {
        if (removedItem != null) {
            System.out.println("Undone removal: " + removedItem.getName());
            List<GroceryItem> newItems = new ArrayList<>(items);
            newItems.add(removedItem);
            return newItems;
        } else {
            System.out.println("Cannot undo removal: " + item.getName());
            return items;
        }
    }
}

