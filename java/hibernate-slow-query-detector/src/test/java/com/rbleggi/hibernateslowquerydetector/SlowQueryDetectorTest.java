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
}
