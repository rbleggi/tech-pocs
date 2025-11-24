package com.rbleggi.loggerrouter;

import java.util.concurrent.ExecutorService;

public class AsyncStrategy implements LogStrategy {
    private final LogStrategy inner;
    private final ExecutorService executor;

    public AsyncStrategy(LogStrategy inner, ExecutorService executor) {
        this.inner = inner;
        this.executor = executor;
    }

    @Override
    public void log(LogLevel level, String msg) {
        executor.submit(() -> inner.log(level, msg));
    }
}
