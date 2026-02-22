package com.rbleggi.guitarfactory;

import java.util.HashMap;
import java.util.Map;

final class Guitar {
    private final String guitarType;
    private final String model;
    private final String specs;
    private final String os;

    private Guitar(String guitarType, String model, String specs, String os) {
        this.guitarType = guitarType;
        this.model = model;
        this.specs = specs;
        this.os = os;
    }

    String getGuitarType() { return guitarType; }
    String getModel() { return model; }
    String getSpecs() { return specs; }
    String getOs() { return os; }

    @Override
    public String toString() {
        return "Type: " + guitarType + ", Model: " + model + ", Specs: " + specs + ", OS: " + os;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Guitar guitar = (Guitar) o;
        return guitarType.equals(guitar.guitarType) &&
               model.equals(guitar.model) &&
               specs.equals(guitar.specs) &&
               os.equals(guitar.os);
    }

    @Override
    public int hashCode() {
        int result = guitarType.hashCode();
        result = 31 * result + model.hashCode();
        result = 31 * result + specs.hashCode();
        result = 31 * result + os.hashCode();
        return result;
    }

    static Builder builder() { return new Builder(); }

    static class Builder {
        private String guitarType = "";
        private String model = "";
        private String specs = "";
        private String os = "";

        private Builder() {}

        Builder guitarType(String guitarType) { this.guitarType = guitarType; return this; }
        Builder model(String model) { this.model = model; return this; }
        Builder specs(String specs) { this.specs = specs; return this; }
        Builder os(String os) { this.os = os; return this; }

        Guitar build() { return new Guitar(guitarType, model, specs, os); }
    }
}

class GuitarInventory {
    private static final GuitarInventory INSTANCE = new GuitarInventory();
    private final Map<Guitar, Integer> inventory = new HashMap<>();

    private GuitarInventory() {}

    static GuitarInventory getInstance() { return INSTANCE; }

    void addGuitar(Guitar guitar, int quantity) {
        inventory.put(guitar, inventory.getOrDefault(guitar, 0) + quantity);
        System.out.println("Added " + quantity + " " + guitar.getModel() + " (" + guitar.getGuitarType() + ") to inventory.");
    }

    void removeGuitar(Guitar guitar, int quantity) {
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

    int getQuantity(Guitar guitar) { return inventory.getOrDefault(guitar, 0); }

    void clearInventory() { inventory.clear(); }

    void listInventory() {
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

public class Main {
    public static void main(String[] args) {
        System.out.println("Guitar Factory");
    }
}
