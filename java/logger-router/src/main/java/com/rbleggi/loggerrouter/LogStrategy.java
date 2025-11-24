package com.rbleggi.loggerrouter;

public interface LogStrategy {
    void log(LogLevel level, String msg);
}
