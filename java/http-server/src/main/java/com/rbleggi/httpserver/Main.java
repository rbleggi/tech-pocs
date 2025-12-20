package com.rbleggi.httpserver;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

interface GetRouteHandler {
    String handle(String path);
}

class HelloHandler implements GetRouteHandler {
    @Override
    public String handle(String path) {
        return "/hello".equals(path) ? "Hello, World!" : null;
    }
}

class PingHandler implements GetRouteHandler {
    @Override
    public String handle(String path) {
        return "/ping".equals(path) ? "pong" : null;
    }
}

class HttpRouter {
    private final List<GetRouteHandler> handlers = new ArrayList<>();

    public void registerHandler(GetRouteHandler handler) {
        handlers.add(handler);
    }

    public String route(String path) {
        return handlers.stream()
            .map(handler -> handler.handle(path))
            .filter(result -> result != null)
            .findFirst()
            .orElse("404");
    }
}

public class Main {
    public static void main(String[] args) throws Exception {
        int port = 8080;
        var server = new ServerSocket(port);
        System.out.println("HTTP server running on http://localhost:" + port + " ...");

        var handlers = List.of(new HelloHandler(), new PingHandler());

        while (true) {
            var client = server.accept();
            new Thread(() -> handleClient(client, handlers)).start();
        }
    }

    private static void handleClient(Socket client, List<GetRouteHandler> handlers) {
        try (client;
             var input = new BufferedReader(new InputStreamReader(client.getInputStream()));
             var output = new PrintWriter(client.getOutputStream())) {

            String requestLine = input.readLine();
            if (requestLine != null && requestLine.startsWith("GET ")) {
                String path = requestLine.split(" ")[1];
                String response = handlers.stream()
                    .map(handler -> handler.handle(path))
                    .filter(result -> result != null)
                    .findFirst()
                    .orElse("Not Found");
                String status = "Not Found".equals(response) ? "404 Not Found" : "200 OK";
                output.println("HTTP/1.1 " + status + "\r\nContent-Type: text/plain\r\n\r\n" + response);
            } else {
                output.println("HTTP/1.1 400 Bad Request\r\nContent-Type: text/plain\r\n\r\nBad Request");
            }
            output.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
