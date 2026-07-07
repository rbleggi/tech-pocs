package com.rbleggi.wiremock;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.BindMode;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Testcontainers
class MockServicesContainerTest {

    private static final String WIREMOCK_IMAGE = "wiremock/wiremock:3.9.1";
    private static final HttpClient CLIENT = HttpClient.newHttpClient();

    private static GenericContainer<?> paymentService;
    private static GenericContainer<?> userService;
    private static GenericContainer<?> notificationService;

    @BeforeAll
    static void startContainers() {
        paymentService = wireMockContainer("wiremock/payment-service/mappings");
        userService = wireMockContainer("wiremock/user-service/mappings");
        notificationService = wireMockContainer("wiremock/notification-service/mappings");

        paymentService.start();
        userService.start();
        notificationService.start();
    }

    @AfterAll
    static void stopContainers() {
        paymentService.stop();
        userService.stop();
        notificationService.stop();
    }

    private static GenericContainer<?> wireMockContainer(String mappingsClasspathRoot) {
        return new GenericContainer<>(WIREMOCK_IMAGE)
                .withExposedPorts(8080)
                .withClasspathResourceMapping(mappingsClasspathRoot, "/home/wiremock/mappings", BindMode.READ_ONLY)
                .waitingFor(Wait.forHttp("/__admin/mappings").forStatusCode(200));
    }

    private HttpResponse<String> get(GenericContainer<?> container, String path) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://" + container.getHost() + ":" + container.getMappedPort(8080) + path))
                .GET()
                .build();
        return CLIENT.send(request, HttpResponse.BodyHandlers.ofString());
    }

    private HttpResponse<String> post(GenericContainer<?> container, String path) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://" + container.getHost() + ":" + container.getMappedPort(8080) + path))
                .POST(HttpRequest.BodyPublishers.noBody())
                .build();
        return CLIENT.send(request, HttpResponse.BodyHandlers.ofString());
    }

    @Test
    void paymentServiceContainerReturnsApprovedPaymentStatus() throws Exception {
        HttpResponse<String> response = get(paymentService, "/payments/1001");

        assertEquals(200, response.statusCode());
        assertTrue(response.body().contains("APPROVED"));
    }

    @Test
    void userServiceContainerReturnsUserById() throws Exception {
        HttpResponse<String> response = get(userService, "/users/42");

        assertEquals(200, response.statusCode());
        assertTrue(response.body().contains("Joao Silva"));
    }

    @Test
    void notificationServiceContainerQueuesNotification() throws Exception {
        HttpResponse<String> response = post(notificationService, "/notifications");

        assertEquals(202, response.statusCode());
        assertTrue(response.body().contains("QUEUED"));
    }
}
