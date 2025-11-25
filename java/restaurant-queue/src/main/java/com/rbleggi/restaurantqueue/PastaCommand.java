package com.rbleggi.restaurantqueue;

public class PastaCommand implements DishCommand {
    @Override
    public String name() {
        return "Pasta";
    }

    @Override
    public int prepTime() {
        return 15;
    }
}
