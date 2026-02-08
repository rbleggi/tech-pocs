package com.rbleggi.loggerrouter;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

import static org.junit.jupiter.api.Assertions.*;

class LoggerRouterTest {

    @Test
    @DisplayName("LoggerRouter with single ConsoleStrategy should route logs")
    void loggerRouter_singleStrategy_routesLogs() {
        var testLogs = new ArrayList<String>();
        var testStrategy = new TestStrategy(testLogs);
        var logger = new LoggerRouter(List.of(testStrategy));

        logger.log(LogLevel.INFO, "Test message");

        assertEquals(1, testLogs.size());
        assertTrue(testLogs.get(0).contains("INFO"));
        assertTrue(testLogs.get(0).contains("Test message"));
    }

    @Test
    @DisplayName("LoggerRouter with multiple strategies should route to all")
    void loggerRouter_multipleStrategies_routesToAll() {
        var testLogs1 = new ArrayList<String>();
        var testLogs2 = new ArrayList<String>();
        var strategy1 = new TestStrategy(testLogs1);
        var strategy2 = new TestStrategy(testLogs2);
        var logger = new LoggerRouter(List.of(strategy1, strategy2));

        logger.log(LogLevel.ERROR, "Error occurred");

        assertEquals(1, testLogs1.size());
        assertEquals(1, testLogs2.size());
        assertTrue(testLogs1.get(0).contains("ERROR"));
        assertTrue(testLogs2.get(0).contains("ERROR"));
    }

    @Test
    @DisplayName("LoggerRouter should handle different log levels")
    void loggerRouter_differentLogLevels_handlesCorrectly() {
        var testLogs = new ArrayList<String>();
        var testStrategy = new TestStrategy(testLogs);
        var logger = new LoggerRouter(List.of(testStrategy));

        logger.log(LogLevel.INFO, "Info message");
        logger.log(LogLevel.ERROR, "Error message");

        assertEquals(2, testLogs.size());
        assertTrue(testLogs.get(0).contains("INFO"));
        assertTrue(testLogs.get(1).contains("ERROR"));
    }

    @Test
    @DisplayName("ConsoleStrategy should log to console")
    void consoleStrategy_logsToConsole() {
        var strategy = new ConsoleStrategy();
        assertDoesNotThrow(() -> strategy.log(LogLevel.INFO, "Test"));
    }

    @Test
    @DisplayName("FileSystemStrategy should create instance")
    void fileSystemStrategy_createsInstance() {
        var strategy = new FileSystemStrategy("test.log");
        assertNotNull(strategy);
    }

    @Test
    @DisplayName("ELKStrategy should create instance")
    void elkStrategy_createsInstance() {
        var strategy = new ELKStrategy("http://localhost:9200");
        assertNotNull(strategy);
    }

    @Test
    @DisplayName("AsyncStrategy should wrap another strategy")
    void asyncStrategy_wrapsStrategy() throws InterruptedException {
        var executor = Executors.newFixedThreadPool(1);
        var testLogs = new ArrayList<String>();
        var innerStrategy = new TestStrategy(testLogs);
        var asyncStrategy = new AsyncStrategy(innerStrategy, executor);

        asyncStrategy.log(LogLevel.INFO, "Async message");
        Thread.sleep(100);

        assertEquals(1, testLogs.size());
        executor.shutdown();
    }

    private static class TestStrategy implements LogStrategy {
        private final List<String> logs;

        TestStrategy(List<String> logs) {
            this.logs = logs;
        }

        @Override
        public void log(LogLevel level, String msg) {
            logs.add(level + ": " + msg);
        }
    }
}
