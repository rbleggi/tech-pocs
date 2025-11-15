package com.rbleggi.guitarfactory;

public class Main {
    public static void main(String[] args) {
        Guitar guitar1 = Guitar.builder()
                .guitarType("Acoustic")
                .model("Yamaha FG800")
                .specs("Spruce Top, Mahogany Back")
                .os("Custom OS 1.0")
                .build();

        Guitar guitar2 = Guitar.builder()
                .guitarType("Electric")
                .model("Fender Stratocaster")
                .specs("Alder Body, Maple Neck")
                .os("Custom OS 2.0")
                .build();

        Guitar guitar3 = Guitar.builder()
                .guitarType("Bass")
                .model("Ibanez GSRM20")
                .specs("Poplar Body, Maple Neck")
                .os("Custom OS 3.0")
                .build();

        GuitarInventory inventory = GuitarInventory.getInstance();
        inventory.addGuitar(guitar1, 5);
        inventory.addGuitar(guitar2, 3);
        inventory.addGuitar(guitar3, 2);

        inventory.listInventory();

        inventory.removeGuitar(guitar2, 1);
        inventory.removeGuitar(guitar3, 3);

        inventory.listInventory();
    }
}

