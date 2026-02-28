package com.rbleggi.taskthreadpool;

import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

//Thread pool customizado que gerencia um conjunto fixo de worker threads para executar tarefas concorrentemente.
public class Main {
    public static void main(String[] args) {
        System.out.println("Task Threadpool");
    }
}
//A Task e uma interface funcional
interface Task {
    void run();
}
//e o TaskThreadPool usa uma LinkedBlockingQueue para enfileirar tarefas que os workers consomem com polling.
class TaskThreadPool {
    //LinkedBlockingQueue e uma fila thread-safe baseada em linked nodes -
    //operacoes de offer e poll sao bloqueantes e nao precisam de sincronizacao manual,
    //evitando race conditions entre producers (quem submete) e consumers (workers).
    private final LinkedBlockingQueue<Task> taskQueue = new LinkedBlockingQueue<>();
    //O campo isRunning e marcado como volatile, o que garante que quando uma thread altera o valor,
    // todas as outras threads enxergam a mudanca imediatamente -
    // sem volatile, cada thread poderia cachear o valor no seu registrador/CPU cache e nunca perceber o shutdown.
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
            // O offer insere um elemento na fila sem bloquear -
            // se a fila tiver capacidade, adiciona e retorna true;
            // se estiver cheia, retorna false sem esperar.
            taskQueue.offer(task);
        }
    }
    //O shutdown sinaliza via a flag volatile e faz join em cada worker.
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
                    //O poll com timeout faz o oposto: tenta retirar um elemento da fila, e se estiver vazia,
                    // bloqueia ate o timeout (100ms no caso) antes de retornar null,
                    // evitando busy-waiting que consumiria CPU desnecessariamente.
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

//A extensao PriorityTaskThreadPool troca a fila por PriorityBlockingQueue -
// uma fila thread-safe que ordena os elementos automaticamente usando Comparable.
class PriorityTaskThreadPool {
    //Diferente da LinkedBlockingQueue que e FIFO (primeiro a entrar, primeiro a sair),
    // a PriorityBlockingQueue sempre entrega primeiro o elemento de menor valor -
    // no caso, tarefas com menor numero de prioridade sao executadas antes,
    // usando o compareTo do record PriorityTask.
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
