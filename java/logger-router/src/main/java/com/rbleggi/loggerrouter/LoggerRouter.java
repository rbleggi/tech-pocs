package com.rbleggi.loggerrouter;

import java.util.List;

public class LoggerRouter {
    private final List<LogStrategy> strategies;

    public LoggerRouter(List<LogStrategy> strategies) {
        this.strategies = strategies;
    }

    public void log(LogLevel level, String msg) {
        strategies.forEach(strategy -> strategy.log(level, msg));
    }
}
