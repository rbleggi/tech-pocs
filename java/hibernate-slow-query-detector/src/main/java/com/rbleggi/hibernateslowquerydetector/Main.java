package com.rbleggi.hibernateslowquerydetector;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

interface QueryObserver {
    void notify(String query, long durationMs);
}

class SlowQueryDetector {
    private final long thresholdMs;
    private final List<QueryObserver> observers = new ArrayList<>();
    private int slowQueryCount = 0;

    public SlowQueryDetector(long thresholdMs) {
        this.thresholdMs = thresholdMs;
    }

    public void addObserver(QueryObserver observer) {
        observers.add(observer);
    }

    public void removeObserver(QueryObserver observer) {
        observers.remove(observer);
    }

    public void executeQuery(String query, long simulatedDurationMs) {
        if (simulatedDurationMs > thresholdMs) {
            slowQueryCount++;
            observers.forEach(observer -> observer.notify(query, simulatedDurationMs));
        }
    }

    public int getSlowQueryCount() {
        return slowQueryCount;
    }
}

class ConsoleLogger implements QueryObserver {
    @Override
    public void notify(String query, long durationMs) {
        System.out.println("SLOW QUERY DETECTED: [" + query + "] took " + durationMs + " ms");
    }
}

class FileLogger implements QueryObserver {
    private final String filename;

    public FileLogger(String filename) {
        this.filename = filename;
    }

    @Override
    public void notify(String query, long durationMs) {
        try (var writer = new PrintWriter(new FileWriter(filename, true))) {
            writer.println("SLOW QUERY: [" + query + "] - " + durationMs + " ms");
        } catch (IOException e) {
            System.err.println("Failed to write to log file: " + e.getMessage());
        }
    }
}

class EmailNotifier implements QueryObserver {
    private final String emailAddress;

    public EmailNotifier(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    @Override
    public void notify(String query, long durationMs) {
        System.out.println("Sending email to " + emailAddress + ": Slow query [" + query + "] took " + durationMs + " ms");
    }
}

public class Main {
    public static void main(String[] args) {
        var detector = new SlowQueryDetector(100);
        detector.addObserver(new ConsoleLogger());
        detector.addObserver(new FileLogger("slow-queries.log"));
        detector.addObserver(new EmailNotifier("admin@example.com"));

        detector.executeQuery("SELECT * FROM users", 50);
        detector.executeQuery("SELECT * FROM orders", 200);
        detector.executeQuery("UPDATE products SET price = 10", 300);
    }
}
