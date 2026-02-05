package com.rbleggi.hibernateslowquerydetector;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class SlowQueryDetectorTest {

    @Test
    void shouldNotifyObserverForSlowQuery() {
        var notified = new boolean[]{false};
        var detector = new SlowQueryDetector(100);

        detector.addObserver((query, durationMs) -> notified[0] = true);
        detector.executeQuery("SELECT * FROM orders", 200);

        assertTrue(notified[0]);
    }

    @Test
    void shouldNotNotifyForFastQuery() {
        var notified = new boolean[]{false};
        var detector = new SlowQueryDetector(100);

        detector.addObserver((query, durationMs) -> notified[0] = true);
        detector.executeQuery("SELECT * FROM users", 50);

        assertFalse(notified[0]);
    }

    @Test
    void shouldNotNotifyAtExactThreshold() {
        var notified = new boolean[]{false};
        var detector = new SlowQueryDetector(100);

        detector.addObserver((query, durationMs) -> notified[0] = true);
        detector.executeQuery("SELECT * FROM products", 100);

        assertFalse(notified[0]);
    }

    @Test
    void shouldNotifyMultipleObservers() {
        var notified1 = new boolean[]{false};
        var notified2 = new boolean[]{false};
        var detector = new SlowQueryDetector(100);

        detector.addObserver((query, durationMs) -> notified1[0] = true);
        detector.addObserver((query, durationMs) -> notified2[0] = true);

        detector.executeQuery("SELECT * FROM customers", 150);

        assertTrue(notified1[0]);
        assertTrue(notified2[0]);
    }

    @Test
    void shouldRemoveObserver() {
        var notified = new boolean[]{false};
        var detector = new SlowQueryDetector(100);
        QueryObserver observer = (query, durationMs) -> notified[0] = true;

        detector.addObserver(observer);
        detector.removeObserver(observer);

        detector.executeQuery("SELECT * FROM items", 200);

        assertFalse(notified[0]);
    }

    @Test
    void shouldHandleZeroThreshold() {
        var count = new int[]{0};
        var detector = new SlowQueryDetector(0);

        detector.addObserver((query, durationMs) -> count[0]++);

        detector.executeQuery("SELECT 1", 1);
        detector.executeQuery("SELECT 2", 10);

        assertEquals(2, count[0]);
    }

    @Test
    void shouldHandleNoObservers() {
        var detector = new SlowQueryDetector(100);

        detector.executeQuery("SELECT * FROM test", 200);
    }

    @Test
    void shouldPassCorrectQueryAndDuration() {
        var receivedQuery = new String[1];
        var receivedDuration = new long[1];
        var detector = new SlowQueryDetector(100);

        detector.addObserver((query, durationMs) -> {
            receivedQuery[0] = query;
            receivedDuration[0] = durationMs;
        });

        detector.executeQuery("SELECT * FROM accounts", 250);

        assertEquals("SELECT * FROM accounts", receivedQuery[0]);
        assertEquals(250, receivedDuration[0]);
    }

    @Test
    void shouldHandleMultipleSlowQueries() {
        var count = new int[]{0};
        var detector = new SlowQueryDetector(100);

        detector.addObserver((query, durationMs) -> count[0]++);

        detector.executeQuery("SELECT * FROM table1", 150);
        detector.executeQuery("SELECT * FROM table2", 200);
        detector.executeQuery("SELECT * FROM table3", 120);

        assertEquals(3, count[0]);
    }

    @Test
    void shouldHandleEmptyQueryString() {
        var receivedQuery = new String[1];
        var detector = new SlowQueryDetector(100);

        detector.addObserver((query, durationMs) -> receivedQuery[0] = query);
        detector.executeQuery("", 150);

        assertEquals("", receivedQuery[0]);
    }

    @Test
    void shouldIncrementSlowQueryCounter() {
        var detector = new SlowQueryDetector(100);

        detector.executeQuery("SELECT * FROM table1", 150);
        detector.executeQuery("SELECT * FROM table2", 200);

        assertEquals(2, detector.getSlowQueryCount());
    }

    @Test
    void shouldNotIncrementCounterForFastQueries() {
        var detector = new SlowQueryDetector(100);

        detector.executeQuery("SELECT * FROM table1", 50);
        detector.executeQuery("SELECT * FROM table2", 80);

        assertEquals(0, detector.getSlowQueryCount());
    }

    @Test
    void shouldResetCounter() {
        var detector = new SlowQueryDetector(100);

        detector.executeQuery("SELECT * FROM table1", 150);
        detector.executeQuery("SELECT * FROM table2", 200);
        detector.resetCounter();

        assertEquals(0, detector.getSlowQueryCount());
    }

    @Test
    void shouldTrackQueryMetrics() {
        var detector = new SlowQueryDetector(100);

        detector.executeQuery("SELECT * FROM table1", 50);
        detector.executeQuery("SELECT * FROM table2", 150);
        detector.executeQuery("SELECT * FROM table3", 80);

        var metrics = detector.getMetrics();
        assertEquals(1, metrics.slowCount());
        assertEquals(3, metrics.totalCount());
    }
}
