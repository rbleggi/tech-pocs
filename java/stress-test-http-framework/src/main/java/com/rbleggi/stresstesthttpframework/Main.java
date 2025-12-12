package com.rbleggi.stresstesthttpframework;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class Main {
    public static void main(String[] args) {
        var url = "https://httpbin.org/get";
        var requests = 50;
        var concurrency = 10;
        var test = new HttpStressTest(url, requests, concurrency);
        test.runTest();
    }
}

abstract class StressTestTemplate {
    abstract void prepare();
    abstract void execute();
    abstract void report();

    void runTest() {
        prepare();
        execute();
        report();
    }
}

class HttpStressTest extends StressTestTemplate {
    private final String url;
    private final int requests;
    private final int concurrency;
    private List<Long> results;
    private final HttpClient client;

    HttpStressTest(String url, int requests, int concurrency) {
        this.url = url;
        this.requests = requests;
        this.concurrency = concurrency;
        this.client = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(5))
            .build();
    }

    @Override
    void prepare() {
        results = new ArrayList<>();
    }

    @Override
    void execute() {
        try (ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor()) {
            List<Future<Long>> futures = new ArrayList<>();
            for (int i = 0; i < requests; i++) {
                futures.add(executor.submit(() -> {
                    var start = System.nanoTime();
                    var request = HttpRequest.newBuilder()
                        .uri(URI.create(url))
                        .GET()
                        .build();
                    try {
                        client.send(request, HttpResponse.BodyHandlers.discarding());
                    } catch (Exception e) {
                        System.out.println("Request failed: " + e.getMessage());
                    }
                    var end = System.nanoTime();
                    return end - start;
                }));
            }
            for (var future : futures) {
                try {
                    results.add(future.get());
                } catch (Exception e) {
                    System.out.println("Error collecting result: " + e.getMessage());
                }
            }
        }
    }

    @Override
    void report() {
        var total = results.size();
        var avg = results.isEmpty() ? 0L : (long) results.stream().mapToLong(Long::longValue).average().orElse(0.0);
        System.out.println("Total requests: " + total);
        System.out.println("Average response time (ns): " + avg);
        System.out.println("Min response time (ns): " + (results.isEmpty() ? 0L : results.stream().mapToLong(Long::longValue).min().orElse(0L)));
        System.out.println("Max response time (ns): " + (results.isEmpty() ? 0L : results.stream().mapToLong(Long::longValue).max().orElse(0L)));
    }
}
