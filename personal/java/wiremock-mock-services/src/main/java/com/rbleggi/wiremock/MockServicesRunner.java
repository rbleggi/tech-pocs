package com.rbleggi.wiremock;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;

import java.util.List;

public final class MockServicesRunner {

    private record ServiceMock(String name, int port, String classpathRoot) {
    }

    private static final List<ServiceMock> SERVICES = List.of(
            new ServiceMock("payment-service", 8081, "wiremock/payment-service"),
            new ServiceMock("user-service", 8082, "wiremock/user-service"),
            new ServiceMock("notification-service", 8083, "wiremock/notification-service")
    );

    public static void main(String[] args) throws InterruptedException {
        List<WireMockServer> servers = SERVICES.stream()
                .map(MockServicesRunner::startServer)
                .toList();

        Runtime.getRuntime().addShutdownHook(new Thread(() -> servers.forEach(WireMockServer::stop)));

        System.out.println("Mock services running. Press Ctrl+C to stop.");
        Thread.currentThread().join();
    }

    private static WireMockServer startServer(ServiceMock service) {
        WireMockServer server = new WireMockServer(WireMockConfiguration.wireMockConfig()
                .port(service.port())
                .usingFilesUnderClasspath(service.classpathRoot()));
        server.start();
        System.out.printf("%s mock running at http://localhost:%d%n", service.name(), service.port());
        return server;
    }
}
