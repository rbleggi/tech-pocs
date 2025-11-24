package com.rbleggi.loggerrouter;

import java.util.List;
import java.util.concurrent.Executors;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        var executor = Executors.newFixedThreadPool(2);

        var strategies = List.of(
            new FileSystemStrategy("app.log"),
            new AsyncStrategy(new ELKStrategy("http://localhost:9200"), executor),
            new ConsoleStrategy()
        );

        var logger = new LoggerRouter(strategies);

        logger.log(LogLevel.INFO, "User login successful");
        logger.log(LogLevel.ERROR, "Database connection failed");

        Thread.sleep(500);
        executor.shutdown();
    }
}
