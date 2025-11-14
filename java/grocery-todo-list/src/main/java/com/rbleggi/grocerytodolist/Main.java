package com.rbleggi.grocerytodolist;

public class Main {
    public static void main(String[] args) {
        System.out.println("\nWelcome to Grocery TODO List!");
        System.out.println("--------------------------------");

        GroceryManager manager = new GroceryManager();
        CommandInvoker invoker = new CommandInvoker();

        GroceryItem milkItem = new GroceryItem("Milk");
        GroceryItem breadItem = new GroceryItem("Bread");
        GroceryItem eggsItem = new GroceryItem("Eggs");

        manager.applyChanges(invoker.executeCommand(new AddItemCommand(milkItem), manager.getItems()));
        manager.applyChanges(invoker.executeCommand(new AddItemCommand(breadItem), manager.getItems()));
        manager.applyChanges(invoker.executeCommand(new AddItemCommand(eggsItem), manager.getItems()));

        manager.listAll();
        manager.applyChanges(invoker.executeCommand(new MarkAsDoneCommand(milkItem), manager.getItems()));
        manager.listAll();
        manager.applyChanges(invoker.executeCommand(new RemoveItemCommand(breadItem), manager.getItems()));
        manager.listAll();
        manager.applyChanges(invoker.undo(manager.getItems()));
        manager.listAll();
        manager.applyChanges(invoker.undo(manager.getItems()));
        manager.listAll();
        manager.applyChanges(invoker.redo(manager.getItems()));
        manager.listAll();
    }
}

