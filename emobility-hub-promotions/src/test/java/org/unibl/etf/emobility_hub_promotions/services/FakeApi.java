package org.unibl.etf.emobility_hub_promotions.services;

import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

// A tiny stand-in for the Spring Boot API: answers every request with one
// canned response and records what it received.
final class FakeApi implements AutoCloseable {

    record Received(String method, String uri, String authorization, String body) {
    }

    private final HttpServer server;
    final List<Received> requests = new ArrayList<>();
    private int status = 200;
    private String responseBody = "";

    FakeApi() throws IOException {
        server = HttpServer.create(new InetSocketAddress("127.0.0.1", 0), 0);
        server.createContext("/", exchange -> {
            String body = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
            requests.add(new Received(exchange.getRequestMethod(), exchange.getRequestURI().toString(),
                    exchange.getRequestHeaders().getFirst("Authorization"), body));
            byte[] bytes = responseBody.getBytes(StandardCharsets.UTF_8);
            exchange.getResponseHeaders().add("Content-Type", "application/json");
            exchange.sendResponseHeaders(status, status == 204 || bytes.length == 0 ? -1 : bytes.length);
            if (bytes.length > 0 && status != 204) {
                try (OutputStream os = exchange.getResponseBody()) {
                    os.write(bytes);
                }
            }
            exchange.close();
        });
        server.start();
    }

    FakeApi respond(int status, String body) {
        this.status = status;
        this.responseBody = body;
        return this;
    }

    String url(String path) {
        return "http://127.0.0.1:" + server.getAddress().getPort() + path;
    }

    Received last() {
        return requests.get(requests.size() - 1);
    }

    @Override
    public void close() {
        server.stop(0);
    }
}
