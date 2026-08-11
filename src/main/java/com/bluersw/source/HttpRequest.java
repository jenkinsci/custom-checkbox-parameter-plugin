package com.bluersw.source;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Locale;

import hudson.ProxyConfiguration;

/**
 * 通过HTTP和HTTPS协议获得响应结果 Get response results through HTTP and HTTPS protocols
 * @author sunweisheng
 */
public class HttpRequest implements DataSource {

	private static final int DEFAULT_TIMEOUT_MILLIS = 30_000;
	private final URL url;
	private final int timeoutMillis;
	private String statusLine;
	private int statusCode;

	/**
	 * 通过要请求的URI构建请求对象 Construct the request object by the URI to be requested
	 * @param uri 要请求的URI地址 URL
	 */
	public HttpRequest(String uri) {
		this(uri, DEFAULT_TIMEOUT_MILLIS);
	}

	HttpRequest(String uri, int timeoutMillis) {
		try {
			URI parsed = URI.create(uri);
			String scheme = parsed.getScheme() == null ? "" : parsed.getScheme().toLowerCase(Locale.ROOT);
			if (!("http".equals(scheme) || "https".equals(scheme)) || parsed.getHost() == null) {
				throw new IllegalArgumentException("URI needs to start with http or https");
			}
			if (timeoutMillis <= 0) {
				throw new IllegalArgumentException("Timeout needs to be greater than zero");
			}
			this.url = parsed.toURL();
			this.timeoutMillis = timeoutMillis;
		} catch (IllegalArgumentException e) {
			throw e;
		} catch (Exception e) {
			throw new IllegalArgumentException("Invalid HTTP or HTTPS URI", e);
		}
	}

	/**
	 * 获得请求结果HTTP状态的字符串说明 Get the string description of the HTTP status of the request result
	 * @return HTTP状态的字符串说明 String description of HTTP status
	 */
	@Override
	public String getStatusLine() {
		return statusLine;
	}

	/**
	 * 获得请求结果HTTP状态码 Get request result HTTP status code
	 * @return HTTP状态码 HTTP status code
	 */
	@Override
	public int getStatusCode() {
		return statusCode;
	}

	/**
	 * 获得GET请求的响应结果 Get the response result of GET request
	 * @return 响应结果 Response results
	 * @throws Exception 异常 Exception
	 */
	@Override
	public String get() throws Exception {
		HttpURLConnection connection = (HttpURLConnection) ProxyConfiguration.open(this.url);
		connection.setConnectTimeout(this.timeoutMillis);
		connection.setReadTimeout(this.timeoutMillis);
		connection.setInstanceFollowRedirects(true);
		connection.setRequestMethod("GET");
		try {
			this.statusCode = connection.getResponseCode();
			this.statusLine = connection.getHeaderField(0);
			if (this.statusCode == HttpURLConnection.HTTP_OK) {
				try (InputStream input = connection.getInputStream()) {
					return new String(input.readAllBytes(), Charset.defaultCharset());
				}
			}
			throw new IOException("HTTP or HTTPS Get data failed,HttpStatus:" + this.statusLine);
		} finally {
			connection.disconnect();
		}
	}
}
