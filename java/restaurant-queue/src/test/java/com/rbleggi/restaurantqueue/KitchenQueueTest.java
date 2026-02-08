package com.rbleggi.restaurantqueue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class KitchenQueueTest {
    private KitchenQueue queue;

    @BeforeEach
    void setUp() {
        queue = new KitchenQueue();
    }

    @Test
    @DisplayName("BurgerCommand should return correct name and prep time")
    void burgerCommand_returnsCorrectValues() {
        var burger = new BurgerCommand();
        assertEquals("Burger", burger.name());
        assertEquals(12, burger.prepTime());
    }

    @Test
    @DisplayName("PastaCommand should return correct name and prep time")
    void pastaCommand_returnsCorrectValues() {
        var pasta = new PastaCommand();
        assertEquals("Pasta", pasta.name());
        assertEquals(15, pasta.prepTime());
    }

    @Test
    @DisplayName("SaladCommand should return correct name and prep time")
    void saladCommand_returnsCorrectValues() {
        var salad = new SaladCommand();
        assertEquals("Salad", salad.name());
        assertEquals(5, salad.prepTime());
    }

    @Test
    @DisplayName("SushiCommand should return correct name and prep time")
    void sushiCommand_returnsCorrectValues() {
        var sushi = new SushiCommand();
        assertEquals("Sushi", sushi.name());
        assertEquals(20, sushi.prepTime());
    }

    @Test
    @DisplayName("KitchenQueue should add and run single command")
    void kitchenQueue_singleCommand_runsSuccessfully() {
        queue.addCommand(new BurgerCommand());
        assertDoesNotThrow(() -> queue.runQueue());
    }

    @Test
    @DisplayName("KitchenQueue should add and run multiple commands")
    void kitchenQueue_multipleCommands_runsSuccessfully() {
        queue.addCommand(new BurgerCommand());
        queue.addCommand(new PastaCommand());
        queue.addCommand(new SaladCommand());
        queue.addCommand(new SushiCommand());
        assertDoesNotThrow(() -> queue.runQueue());
    }

    @Test
    @DisplayName("Empty KitchenQueue should run without errors")
    void kitchenQueue_empty_runsWithoutErrors() {
        assertDoesNotThrow(() -> queue.runQueue());
    }
}
