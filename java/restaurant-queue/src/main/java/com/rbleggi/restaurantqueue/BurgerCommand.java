package com.rbleggi.restaurantqueue;

public class BurgerCommand implements DishCommand {
    @Override
    public String name() {
        return "Burger";
    }

    @Override
    public int prepTime() {
        return 12;
    }
}
