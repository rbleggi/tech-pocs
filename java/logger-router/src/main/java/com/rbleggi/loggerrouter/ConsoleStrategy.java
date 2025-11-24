package com.rbleggi.loggerrouter;

public class ConsoleStrategy implements LogStrategy {
    @Override
    public void log(LogLevel level, String msg) {
        System.out.println("[Console] " + level + ": " + msg);
    }
}
