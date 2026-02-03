package com.rbleggi.guitarfactory;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GuitarFactoryTest {

    @BeforeEach
    void setUp() {
        GuitarInventory.getInstance().clearInventory();
    }

    @Test
    void guitarBuilderCreatesCorrectGuitarInstance() {
        Guitar guitar = Guitar.builder()
                .guitarType("Electric")
                .model("Fender Stratocaster")
                .specs("Alder Body, Maple Neck")
                .os("Custom OS 2.0")
                .build();
        assertEquals("Electric", guitar.getGuitarType());
        assertEquals("Fender Stratocaster", guitar.getModel());
        assertEquals("Alder Body, Maple Neck", guitar.getSpecs());
        assertEquals("Custom OS 2.0", guitar.getOs());
    }

    @Test
    void guitarBuilderWithFluentInterface() {
        Guitar guitar = Guitar.builder()
                .guitarType("Acoustic")
                .model("Yamaha FG800")
                .specs("Spruce Top, Mahogany Back")
                .os("Custom OS 1.0")
                .build();

        assertEquals("Acoustic", guitar.getGuitarType());
        assertEquals("Yamaha FG800", guitar.getModel());
    }

    @Test
    void guitarInventoryAddsGuitarsCorrectly() {
        Guitar guitar = Guitar.builder()
                .guitarType("Bass")
                .model("Ibanez GSRM20")
                .specs("Poplar Body, Maple Neck")
                .os("Custom OS 3.0")
                .build();

        GuitarInventory inventory = GuitarInventory.getInstance();
        inventory.addGuitar(guitar, 2);
        assertEquals(2, inventory.getQuantity(guitar));
    }

    @Test
    void guitarInventoryRemovesGuitarsCorrectly() {
        Guitar guitar = Guitar.builder()
                .guitarType("Electric")
                .model("Gibson Les Paul")
                .specs("Mahogany Body, Rosewood Fretboard")
                .os("Custom OS 4.0")
                .build();

        GuitarInventory inventory = GuitarInventory.getInstance();
        inventory.addGuitar(guitar, 5);
        assertEquals(5, inventory.getQuantity(guitar));

        inventory.removeGuitar(guitar, 2);
        assertEquals(3, inventory.getQuantity(guitar));
    }

    @Test
    void guitarInventoryHandlesRemovingMoreGuitarsThanAvailable() {
        Guitar guitar = Guitar.builder()
                .guitarType("Bass")
                .model("Fender Jazz Bass")
                .specs("Alder Body, Maple Neck")
                .os("Custom OS 5.0")
                .build();

        GuitarInventory inventory = GuitarInventory.getInstance();
        inventory.addGuitar(guitar, 2);
        inventory.removeGuitar(guitar, 1);
        inventory.removeGuitar(guitar, 2);
        assertEquals(1, inventory.getQuantity(guitar));
    }

    @Test
    void guitarInventoryIsSingleton() {
        GuitarInventory inventory1 = GuitarInventory.getInstance();
        GuitarInventory inventory2 = GuitarInventory.getInstance();
        assertSame(inventory1, inventory2);
    }

    @Test
    void guitarToStringFormat() {
        Guitar guitar = Guitar.builder()
                .guitarType("Acoustic")
                .model("Taylor 214ce")
                .specs("Sitka Spruce Top")
                .os("Custom OS 6.0")
                .build();

        String expected = "Type: Acoustic, Model: Taylor 214ce, Specs: Sitka Spruce Top, OS: Custom OS 6.0";
        assertEquals(expected, guitar.toString());
    }

    @Test
    void guitarEquality() {
        Guitar guitar1 = Guitar.builder()
                .guitarType("Electric")
                .model("Fender Telecaster")
                .specs("Ash Body")
                .os("Custom OS 7.0")
                .build();

        Guitar guitar2 = Guitar.builder()
                .guitarType("Electric")
                .model("Fender Telecaster")
                .specs("Ash Body")
                .os("Custom OS 7.0")
                .build();

        assertEquals(guitar1, guitar2);
    }

    @Test
    void guitarInventoryListInventory() {
        GuitarInventory inventory = GuitarInventory.getInstance();
        assertDoesNotThrow(inventory::listInventory);
    }

    @Test
    void guitarInventoryClearInventory() {
        Guitar guitar = Guitar.builder()
                .guitarType("Electric")
                .model("PRS Custom 24")
                .specs("Mahogany Body")
                .os("Custom OS 8.0")
                .build();

        GuitarInventory inventory = GuitarInventory.getInstance();
        inventory.addGuitar(guitar, 3);
        inventory.clearInventory();
        assertEquals(0, inventory.getQuantity(guitar));
    }

    @Test
    void guitarInequality() {
        Guitar guitar1 = Guitar.builder()
                .guitarType("Electric")
                .model("Fender Telecaster")
                .specs("Ash Body")
                .os("Custom OS 7.0")
                .build();

        Guitar guitar2 = Guitar.builder()
                .guitarType("Electric")
                .model("Fender Stratocaster")
                .specs("Ash Body")
                .os("Custom OS 7.0")
                .build();

        assertNotEquals(guitar1, guitar2);
    }

    @Test
    void guitarHashCode() {
        Guitar guitar1 = Guitar.builder()
                .guitarType("Electric")
                .model("Fender Telecaster")
                .specs("Ash Body")
                .os("Custom OS 7.0")
                .build();

        Guitar guitar2 = Guitar.builder()
                .guitarType("Electric")
                .model("Fender Telecaster")
                .specs("Ash Body")
                .os("Custom OS 7.0")
                .build();

        assertEquals(guitar1.hashCode(), guitar2.hashCode());
    }

    @Test
    void guitarInventoryRemoveNonExistent() {
        Guitar guitar = Guitar.builder()
                .guitarType("Electric")
                .model("ESP LTD")
                .specs("Mahogany Body")
                .os("Custom OS 9.0")
                .build();

        GuitarInventory inventory = GuitarInventory.getInstance();
        inventory.removeGuitar(guitar, 1);
        assertEquals(0, inventory.getQuantity(guitar));
    }

    @Test
    void guitarInventoryAddMultipleQuantities() {
        Guitar guitar = Guitar.builder()
                .guitarType("Acoustic")
                .model("Martin D-28")
                .specs("Sitka Spruce Top")
                .os("Custom OS 10.0")
                .build();

        GuitarInventory inventory = GuitarInventory.getInstance();
        inventory.addGuitar(guitar, 3);
        inventory.addGuitar(guitar, 2);
        assertEquals(5, inventory.getQuantity(guitar));
    }
}

