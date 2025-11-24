package com.rbleggi.loggerrouter;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class FileSystemStrategy implements LogStrategy {
    private final String path;

    public FileSystemStrategy(String path) {
        this.path = path;
    }

    @Override
    public void log(LogLevel level, String msg) {
        try (var bw = new BufferedWriter(new FileWriter(path, true))) {
            bw.write("[" + level + "] " + msg + "\n");
        } catch (IOException e) {
            System.err.println("Failed to write to file: " + e.getMessage());
        }
    }
}
