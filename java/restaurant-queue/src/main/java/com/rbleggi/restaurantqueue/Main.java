package com.rbleggi.restaurantqueue;

public class Main {
    public static void main(String[] args) {
        var queue = new KitchenQueue();

        queue.addCommand(new BurgerCommand());
        queue.addCommand(new PastaCommand());
        queue.addCommand(new SaladCommand());
        queue.addCommand(new SushiCommand());

        queue.runQueue();
    }
}
