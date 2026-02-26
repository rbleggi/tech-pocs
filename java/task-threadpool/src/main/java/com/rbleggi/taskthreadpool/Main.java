package com.rbleggi.taskthreadpool;

import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

public class Main {
    public static void main(String[] args) {
        System.out.println("Task Threadpool");
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

record PriorityTask(int priority, Task task) implements Comparable<PriorityTask> {
    @Override
    public int compareTo(PriorityTask other) {
        return Integer.compare(this.priority, other.priority);
    }
}

class PriorityTaskThreadPool {
    private final PriorityBlockingQueue<PriorityTask> taskQueue = new PriorityBlockingQueue<>();
    private volatile boolean isRunning = true;
    private final List<Thread> workers;

    PriorityTaskThreadPool(int workerCount) {
        workers = IntStream.range(0, workerCount)
            .mapToObj(i -> new Thread(this::workerLoop))
            .toList();
        workers.forEach(Thread::start);
    }

    void submit(int priority, Task task) {
        synchronized (this) {
            if (!isRunning) throw new IllegalStateException("ThreadPool is shutting down");
            taskQueue.offer(new PriorityTask(priority, task));
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

    private void workerLoop() {
        while (isRunning || !taskQueue.isEmpty()) {
            try {
                var pt = taskQueue.poll(100, TimeUnit.MILLISECONDS);
                if (pt != null) {
                    pt.task().run();
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}
