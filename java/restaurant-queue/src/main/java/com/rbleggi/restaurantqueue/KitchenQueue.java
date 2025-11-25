package com.rbleggi.restaurantqueue;

import java.util.ArrayList;
import java.util.List;

public class KitchenQueue {
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
