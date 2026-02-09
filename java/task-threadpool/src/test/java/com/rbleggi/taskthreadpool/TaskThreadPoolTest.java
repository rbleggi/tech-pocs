package com.rbleggi.taskthreadpool;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

class TaskThreadPoolTest {

    @Test
    @DisplayName("TaskThreadPool should execute single task")
    void taskThreadPool_singleTask_executesTask() throws InterruptedException {
        var pool = new TaskThreadPool(1);
        var executed = new boolean[]{false};

        pool.submit(() -> executed[0] = true);
        Thread.sleep(100);
        pool.shutdown();

        assertTrue(executed[0]);
    }

    @Test
    @DisplayName("TaskThreadPool should execute multiple tasks")
    void taskThreadPool_multipleTasks_executesAll() throws InterruptedException {
        var pool = new TaskThreadPool(2);
        var executedCount = new int[]{0};
        var latch = new CountDownLatch(5);

        for (int i = 0; i < 5; i++) {
            pool.submit(() -> {
                executedCount[0]++;
                latch.countDown();
            });
        }

        assertTrue(latch.await(2, TimeUnit.SECONDS));
        pool.shutdown();
        assertEquals(5, executedCount[0]);
    }

    @Test
    @DisplayName("TaskThreadPool should handle concurrent task execution")
    void taskThreadPool_concurrentTasks_executesCorrectly() throws InterruptedException {
        var pool = new TaskThreadPool(4);
        var results = Collections.synchronizedList(new ArrayList<Integer>());
        var latch = new CountDownLatch(10);

        for (int i = 0; i < 10; i++) {
            final int taskId = i;
            pool.submit(() -> {
                results.add(taskId);
                latch.countDown();
            });
        }

        assertTrue(latch.await(2, TimeUnit.SECONDS));
        pool.shutdown();
        assertEquals(10, results.size());
    }

    @Test
    @DisplayName("TaskThreadPool should throw exception when submitting after shutdown")
    void taskThreadPool_submitAfterShutdown_throwsException() throws InterruptedException {
        var pool = new TaskThreadPool(1);
        pool.shutdown();

        assertThrows(IllegalStateException.class, () ->
            pool.submit(() -> System.out.println("Task")));
    }

    @Test
    @DisplayName("TaskThreadPool should complete all queued tasks before shutdown")
    void taskThreadPool_shutdown_completesQueuedTasks() throws InterruptedException {
        var pool = new TaskThreadPool(1);
        var executedCount = new int[]{0};

        for (int i = 0; i < 5; i++) {
            pool.submit(() -> {
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
                executedCount[0]++;
            });
        }

        pool.shutdown();
        assertEquals(5, executedCount[0]);
    }

    @Test
    @DisplayName("Task interface should be functional")
    void task_interface_isFunctional() {
        Task task = () -> System.out.println("Task executed");
        assertDoesNotThrow(task::run);
    }
}
