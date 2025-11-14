package com.rbleggi.grocerytodolist;

import java.util.Objects;

public final class GroceryItem {
    private final String name;
    private final boolean done;

    public GroceryItem(String name) { this(name, false); }

    public GroceryItem(String name, boolean done) {
        String n = Objects.requireNonNull(name, "name").trim();
        if (n.isEmpty()) throw new IllegalArgumentException("name cannot be empty");
        this.name = n;
        this.done = done;
    }

    public String getName() { return name; }
    public boolean isDone() { return done; }

    public GroceryItem markAsDone() { return done ? this : new GroceryItem(name, true); }
    public GroceryItem markAsUndone() { return !done ? this : new GroceryItem(name, false); }

    @Override public String toString() { return (done ? "[X] " : "[ ] ") + name; }

    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GroceryItem that = (GroceryItem) o;
        return name.equals(that.name);
    }

    @Override public int hashCode() { return name.hashCode(); }
}

