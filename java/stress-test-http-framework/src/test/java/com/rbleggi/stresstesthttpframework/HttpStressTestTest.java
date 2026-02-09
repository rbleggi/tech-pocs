package com.rbleggi.stresstesthttpframework;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class HttpStressTestTest {

    @Test
    @DisplayName("HttpStressTest should extend StressTestTemplate")
    void httpStressTest_extendsTemplate() {
        var test = new HttpStressTest("http://example.com", 5, 2);
        assertInstanceOf(StressTestTemplate.class, test);
    }

    @Test
    @DisplayName("HttpStressTest should accept configuration parameters")
    void httpStressTest_acceptsParameters() {
        var test = new HttpStressTest("http://example.com", 10, 5);
        assertNotNull(test);
    }

    @Test
    @DisplayName("StressTestTemplate runTest should execute lifecycle methods")
    void stressTestTemplate_runTest_executesLifecycle() {
        var test = new TestStressTest();
        test.runTest();
        assertTrue(test.prepareCalled);
        assertTrue(test.executeCalled);
        assertTrue(test.reportCalled);
    }

    @Test
    @DisplayName("StressTestTemplate should execute methods in order")
    void stressTestTemplate_runTest_executesInOrder() {
        var test = new TestStressTest();
        test.runTest();
        assertTrue(test.prepareOrder < test.executeOrder);
        assertTrue(test.executeOrder < test.reportOrder);
    }

    private static class TestStressTest extends StressTestTemplate {
        boolean prepareCalled = false;
        boolean executeCalled = false;
        boolean reportCalled = false;
        int prepareOrder = 0;
        int executeOrder = 0;
        int reportOrder = 0;
        private int callCounter = 1;

        @Override
        void prepare() {
            prepareCalled = true;
            prepareOrder = callCounter++;
        }

        @Override
        void execute() {
            executeCalled = true;
            executeOrder = callCounter++;
        }

        @Override
        void report() {
            reportCalled = true;
            reportOrder = callCounter++;
        }
    }
}
