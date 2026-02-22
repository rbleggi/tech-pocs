package com.rbleggi.restaurantqueue;

import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        System.out.println("Restaurant Queue");
    }
}

interface DishCommand {
    String name();
    int prepTime();
}

class BurgerCommand implements DishCommand {
    @Override
    public String name() {
        return "Burger";
    }

    @Override
    public int prepTime() {
        return 12;
    }
}

class PastaCommand implements DishCommand {
    @Override
    public String name() {
        return "Pasta";
    }

    @Override
    public int prepTime() {
        return 15;
    }
}

class SaladCommand implements DishCommand {
    @Override
    public String name() {
        return "Salad";
    }

    @Override
    public int prepTime() {
        return 5;
    }
}

class SushiCommand implements DishCommand {
    @Override
    public String name() {
        return "Sushi";
    }

    @Override
    public int prepTime() {
        return 20;
    }
}

class KitchenQueue {
    private final List<DishCommand> commands = new ArrayList<>();

    public void addCommand(DishCommand cmd) {
        commands.add(cmd);
    }

    public void runQueue() {
        System.out.println("\nkitchen queue\n");
        int totalTime = 0;
        for (var cmd : commands) {
            int time = cmd.prepTime();
            System.out.println("Preparing " + cmd.name() + " takes " + time + " minutes.");
            totalTime += time;
        }
        System.out.println("\nAll dishes prepared in " + totalTime + " minutes!");
    }
}
