package com.rbleggi.loggerrouter;

public class ELKStrategy implements LogStrategy {
    private final String endpoint;

    public ELKStrategy(String endpoint) {
        this.endpoint = endpoint;
    }

    @Override
    public void log(LogLevel level, String msg) {
        System.out.println("[ELK @ " + endpoint + "] " + level + ": " + msg);
    }
}
