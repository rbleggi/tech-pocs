package com.rbleggi.taskthreadpool

import java.util.concurrent.LinkedBlockingQueue

interface Task {
    fun run()
}

class TaskThreadPool(workerCount: Int) {
    private val taskQueue = LinkedBlockingQueue<Task>()
    @Volatile private var isRunning = true
    private val workers = List(workerCount) { Worker() }

    init {
        workers.forEach { it.start() }
    }

    fun submit(task: Task) {
        synchronized(this) {
            if (!isRunning) throw IllegalStateException("ThreadPool is shutting down")
            taskQueue.offer(task)
        }
    }

    fun shutdown() {
        synchronized(this) {
            isRunning = false
        }
        workers.forEach { it.join() }
    }

    private inner class Worker : Thread() {
        override fun run() {
            while (isRunning || taskQueue.isNotEmpty()) {
                val task = taskQueue.poll(100, java.util.concurrent.TimeUnit.MILLISECONDS)
                task?.run()
            }
        }
    }
}

fun main() {
    val pool = TaskThreadPool(4)
    (1..10).forEach { i ->
        pool.submit(object : Task {
            override fun run() {
                println("Task $i running on thread ${Thread.currentThread().name}")
            }
        })
    }
    Thread.sleep(1000)
    pool.shutdown()
}
