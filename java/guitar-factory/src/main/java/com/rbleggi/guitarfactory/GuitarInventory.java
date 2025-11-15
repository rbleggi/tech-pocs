package com.rbleggi.guitarfactory;

import java.util.HashMap;
import java.util.Map;

public class GuitarInventory {
    private static final GuitarInventory INSTANCE = new GuitarInventory();
    private final Map<Guitar, Integer> inventory = new HashMap<>();

    private GuitarInventory() {}

    public static GuitarInventory getInstance() {
        return INSTANCE;
    }

    public void addGuitar(Guitar guitar, int quantity) {
        inventory.put(guitar, inventory.getOrDefault(guitar, 0) + quantity);
        System.out.println("Added " + quantity + " " + guitar.getModel() + " (" + guitar.getGuitarType() + ") to inventory.");
    }

    public void removeGuitar(Guitar guitar, int quantity) {
        Integer count = inventory.get(guitar);
        if (count != null && count >= quantity) {
            inventory.put(guitar, count - quantity);
            System.out.println("Removed " + quantity + " " + guitar.getModel() + " (" + guitar.getGuitarType() + ") from inventory.");
        } else if (count != null) {
            System.out.println("Not enough stock for " + guitar.getModel() + " (" + guitar.getGuitarType() + ").");
        } else {
            System.out.println("Guitar " + guitar.getModel() + " (" + guitar.getGuitarType() + ") not found in inventory.");
        }
    }

    public int getQuantity(Guitar guitar) {
        return inventory.getOrDefault(guitar, 0);
    }

    public void clearInventory() {
        inventory.clear();
    }

    public void listInventory() {
        System.out.println("\nCurrent Guitar Inventory:");
        if (inventory.isEmpty()) {
            System.out.println("No guitars in stock.");
        } else {
            inventory.forEach((guitar, count) ->
                System.out.println(guitar.toString() + ", Quantity: " + count)
            );
        }
    }
}

