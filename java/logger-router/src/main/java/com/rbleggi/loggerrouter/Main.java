package com.rbleggi.loggerrouter;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutorService;

public class Main {
    public static void main(String[] args) {
        System.out.println("Logger Router");
    }
}

enum LogLevel {
    INFO, ERROR
}

interface LogStrategy {
    void log(LogLevel level, String msg);
}

class ConsoleStrategy implements LogStrategy {
    @Override
    public void log(LogLevel level, String msg) {
        System.out.println("[Console] " + level + ": " + msg);
    }
}

class FileSystemStrategy implements LogStrategy {
    private final String path;

    FileSystemStrategy(String path) {
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

class ELKStrategy implements LogStrategy {
    private final String endpoint;

    ELKStrategy(String endpoint) {
        this.endpoint = endpoint;
    }

    @Override
    public void log(LogLevel level, String msg) {
        System.out.println("[ELK @ " + endpoint + "] " + level + ": " + msg);
    }
}

class AsyncStrategy implements LogStrategy {
    private final LogStrategy inner;
    private final ExecutorService executor;

    AsyncStrategy(LogStrategy inner, ExecutorService executor) {
        this.inner = inner;
        this.executor = executor;
    }

    @Override
    public void log(LogLevel level, String msg) {
        executor.submit(() -> inner.log(level, msg));
    }
}

class LoggerRouter {
    private final List<LogStrategy> strategies;

    LoggerRouter(List<LogStrategy> strategies) {
        this.strategies = strategies;
    }

    public void log(LogLevel level, String msg) {
        strategies.forEach(strategy -> strategy.log(level, msg));
    }
}
