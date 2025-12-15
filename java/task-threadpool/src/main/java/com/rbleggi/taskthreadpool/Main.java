package com.rbleggi.taskthreadpool;

import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        var pool = new TaskThreadPool(4);
        IntStream.rangeClosed(1, 10).forEach(i -> pool.submit(() -> {
            System.out.println("Task " + i + " running on thread " + Thread.currentThread().getName());
        }));
        Thread.sleep(1000);
        pool.shutdown();
    }
}

interface Task {
    void run();
}

class TaskThreadPool {
    private final LinkedBlockingQueue<Task> taskQueue = new LinkedBlockingQueue<>();
    private volatile boolean isRunning = true;
    private final List<Worker> workers;

    TaskThreadPool(int workerCount) {
        workers = IntStream.range(0, workerCount)
            .mapToObj(i -> new Worker())
            .toList();
        workers.forEach(Worker::start);
    }

    void submit(Task task) {
        synchronized (this) {
            if (!isRunning) throw new IllegalStateException("ThreadPool is shutting down");
            taskQueue.offer(task);
        }
    }

    void shutdown() throws InterruptedException {
        synchronized (this) {
            isRunning = false;
        }
        for (var worker : workers) {
            worker.join();
        }
    }

    private class Worker extends Thread {
        @Override
        public void run() {
            while (isRunning || !taskQueue.isEmpty()) {
                try {
                    var task = taskQueue.poll(100, TimeUnit.MILLISECONDS);
                    if (task != null) {
                        task.run();
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }
    }
}
