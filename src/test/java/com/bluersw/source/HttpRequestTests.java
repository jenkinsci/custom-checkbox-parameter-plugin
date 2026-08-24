package com.bluersw.source;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.SocketTimeoutException;
import java.nio.charset.StandardCharsets;

import com.sun.net.httpserver.HttpServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class HttpRequestTests {

	private HttpServer server;
	private String serverUrl;

	@BeforeEach
	void setUp() throws IOException {
		server = HttpServer.create(new InetSocketAddress("127.0.0.1", 0), 0);
		server.createContext("/config", exchange -> {
			byte[] response = "projects: []\n".getBytes(StandardCharsets.UTF_8);
			exchange.sendResponseHeaders(200, response.length);
			try (OutputStream output = exchange.getResponseBody()) {
				output.write(response);
			}
		});
		server.createContext("/failure", exchange -> {
			exchange.sendResponseHeaders(503, -1);
			exchange.close();
		});
		server.createContext("/slow", exchange -> {
			try {
				Thread.sleep(200);
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
			}
			exchange.sendResponseHeaders(200, -1);
			exchange.close();
		});
		server.start();
		serverUrl = "http://127.0.0.1:" + server.getAddress().getPort();
	}

	@AfterEach
	void tearDown() {
		server.stop(0);
	}

	@Test
	void getsHttpContent() throws Exception {
		HttpRequest request = new HttpRequest(serverUrl + "/config");

		assertEquals("projects: []\n", request.get());
		assertEquals("HTTP/1.1 200 OK", request.getStatusLine());
		assertEquals(200, request.getStatusCode());
	}

	@Test
	void rejectsUnsupportedProtocols() {
		assertThrows(IllegalArgumentException.class, () -> new HttpRequest("file:///tmp/config.yaml"));
	}

	@Test
	void reportsNonSuccessfulResponses() {
		HttpRequest request = new HttpRequest(serverUrl + "/failure");

		IOException error = assertThrows(IOException.class, request::get);
		assertTrue(error.getMessage().contains("503"));
		assertEquals(503, request.getStatusCode());
	}

	@Test
	void timesOutSlowResponses() {
		HttpRequest request = new HttpRequest(serverUrl + "/slow", 50);

		assertThrows(SocketTimeoutException.class, request::get);
	}
}
