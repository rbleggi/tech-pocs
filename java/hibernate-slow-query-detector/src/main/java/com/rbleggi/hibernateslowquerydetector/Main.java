package com.rbleggi.hibernateslowquerydetector;

import java.util.ArrayList;
import java.util.List;

interface QueryObserver {
    void notify(String query, long durationMs);
}

class SlowQueryDetector {
    private final long thresholdMs;
    private final List<QueryObserver> observers = new ArrayList<>();

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
            observers.forEach(observer -> observer.notify(query, simulatedDurationMs));
        }
    }
}

class ConsoleLogger implements QueryObserver {
    @Override
    public void notify(String query, long durationMs) {
        System.out.println("SLOW QUERY DETECTED: [" + query + "] took " + durationMs + " ms");
    }
}

public class Main {
    public static void main(String[] args) {
        var detector = new SlowQueryDetector(100);
        detector.addObserver(new ConsoleLogger());

        detector.executeQuery("SELECT * FROM users", 50);
        detector.executeQuery("SELECT * FROM orders", 200);
        detector.executeQuery("UPDATE products SET price = 10", 300);
    }
}
