package com.rbleggi.wiremock;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MockServicesRunnerTest {

    private final HttpClient client = HttpClient.newHttpClient();

    private HttpResponse<String> get(int port, String path) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:" + port + path))
                .GET()
                .build();
        return client.send(request, HttpResponse.BodyHandlers.ofString());
    }

    private HttpResponse<String> post(int port, String path) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:" + port + path))
                .POST(HttpRequest.BodyPublishers.noBody())
                .build();
        return client.send(request, HttpResponse.BodyHandlers.ofString());
    }

    @Nested
    class PaymentServiceMock {

        private WireMockServer server;

        @BeforeEach
        void startServer() {
            server = new WireMockServer(WireMockConfiguration.wireMockConfig()
                    .port(8081)
                    .usingFilesUnderClasspath("wiremock/payment-service"));
            server.start();
        }

        @AfterEach
        void stopServer() {
            server.stop();
        }

        @Test
        void returnsApprovedPaymentStatus() throws Exception {
            HttpResponse<String> response = get(8081, "/payments/1001");

            assertEquals(200, response.statusCode());
            assertTrue(response.body().contains("APPROVED"));
        }

        @Test
        void createsPayment() throws Exception {
            HttpResponse<String> response = post(8081, "/payments");

            assertEquals(201, response.statusCode());
            assertTrue(response.body().contains("CREATED"));
        }

        @Test
        void simulatesServiceUnavailable() throws Exception {
            HttpResponse<String> response = get(8081, "/payments/unavailable");

            assertEquals(503, response.statusCode());
        }
    }

    @Nested
    class UserServiceMock {

        private WireMockServer server;

        @BeforeEach
        void startServer() {
            server = new WireMockServer(WireMockConfiguration.wireMockConfig()
                    .port(8082)
                    .usingFilesUnderClasspath("wiremock/user-service"));
            server.start();
        }

        @AfterEach
        void stopServer() {
            server.stop();
        }

        @Test
        void returnsUserById() throws Exception {
            HttpResponse<String> response = get(8082, "/users/42");

            assertEquals(200, response.statusCode());
            assertTrue(response.body().contains("Joao Silva"));
        }

        @Test
        void createsUser() throws Exception {
            HttpResponse<String> response = post(8082, "/users");

            assertEquals(201, response.statusCode());
            assertTrue(response.body().contains("Maria Souza"));
        }
    }

    @Nested
    class NotificationServiceMock {

        private WireMockServer server;

        @BeforeEach
        void startServer() {
            server = new WireMockServer(WireMockConfiguration.wireMockConfig()
                    .port(8083)
                    .usingFilesUnderClasspath("wiremock/notification-service"));
            server.start();
        }

        @AfterEach
        void stopServer() {
            server.stop();
        }

        @Test
        void queuesNotification() throws Exception {
            HttpResponse<String> response = post(8083, "/notifications");

            assertEquals(202, response.statusCode());
            assertTrue(response.body().contains("QUEUED"));
        }

        @Test
        void reportsHealthy() throws Exception {
            HttpResponse<String> response = get(8083, "/notifications/health");

            assertEquals(200, response.statusCode());
            assertTrue(response.body().contains("UP"));
        }

        @Test
        void simulatesConnectionFailure() {
            assertThrows(IOException.class, () -> post(8083, "/notifications/unavailable"));
        }
    }
}
