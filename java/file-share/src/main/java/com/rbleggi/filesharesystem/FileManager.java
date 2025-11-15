package com.rbleggi.filesharesystem;

import java.util.HashMap;
import java.util.Map;

public class FileManager {
    private final Map<String, File> files = new HashMap<>();

    public void saveFile(File file) {
        files.put(file.name(), file);
        System.out.println("File '" + file.name() + "' saved.");
    }

    public void restoreFile(File file) {
        if (files.containsKey(file.name())) {
            System.out.println("File '" + file.name() + "' restored.");
        } else {
            System.out.println("File '" + file.name() + "' does not exist.");
        }
    }

    public void deleteFile(File file) {
        if (files.remove(file.name()) != null) {
            System.out.println("File '" + file.name() + "' deleted.");
        } else {
            System.out.println("File '" + file.name() + "' does not exist.");
        }
    }

    public void listFiles() {
        if (files.isEmpty()) {
            System.out.println("\nNo files available.");
        } else {
            System.out.println("\nFiles:");
            files.keySet().forEach(System.out::println);
        }
    }

    public void searchFile(String query) {
        var results = files.keySet().stream()
                .filter(name -> name.contains(query))
                .toList();
        if (results.isEmpty()) {
            System.out.println("\nNo files found matching '" + query + "'.");
        } else {
            System.out.println("\nFiles matching '" + query + "':");
            results.forEach(System.out::println);
        }
    }
}

