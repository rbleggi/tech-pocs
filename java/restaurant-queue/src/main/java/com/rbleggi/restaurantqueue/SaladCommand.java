package com.rbleggi.restaurantqueue;

public class SaladCommand implements DishCommand {
    @Override
    public String name() {
        return "Salad";
    }

    @Override
    public int prepTime() {
        return 5;
    }
}
