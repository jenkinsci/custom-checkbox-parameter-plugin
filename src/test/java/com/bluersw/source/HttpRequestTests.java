package com.bluersw.source;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.SocketTimeoutException;
import java.nio.charset.StandardCharsets;

import com.sun.net.httpserver.HttpServer;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

public class HttpRequestTests {
	private HttpServer server;
	private String serverUrl;

	@Before
	public void setUp() throws IOException {
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

	@After
	public void tearDown() {
		server.stop(0);
	}

	@Test
	public void getsHttpContent() throws Exception {
		HttpRequest request = new HttpRequest(serverUrl + "/config");

		assertEquals("projects: []\n", request.get());
		assertEquals("HTTP/1.1 200 OK", request.getStatusLine());
		assertEquals(200, request.getStatusCode());
	}

	@Test
	public void rejectsUnsupportedProtocols() {
		assertThrows(IllegalArgumentException.class, () -> new HttpRequest("file:///tmp/config.yaml"));
	}

	@Test
	public void reportsNonSuccessfulResponses() {
		HttpRequest request = new HttpRequest(serverUrl + "/failure");

		IOException error = assertThrows(IOException.class, request::get);
		assertTrue(error.getMessage().contains("503"));
		assertEquals(503, request.getStatusCode());
	}

	@Test
	public void timesOutSlowResponses() {
		HttpRequest request = new HttpRequest(serverUrl + "/slow", 50);

		assertThrows(SocketTimeoutException.class, request::get);
	}
}
