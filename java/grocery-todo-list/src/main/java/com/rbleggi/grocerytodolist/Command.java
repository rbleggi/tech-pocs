package com.rbleggi.grocerytodolist;

import java.util.List;

public interface Command {
    List<GroceryItem> execute(List<GroceryItem> items);

    List<GroceryItem> undo(List<GroceryItem> items);
}

