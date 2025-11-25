package com.rbleggi.restaurantqueue;

public class SushiCommand implements DishCommand {
    @Override
    public String name() {
        return "Sushi";
    }

    @Override
    public int prepTime() {
        return 20;
    }
}
