package com.bluersw.util;

import java.nio.charset.Charset;
import java.security.SecureRandom;

import javax.net.ssl.SSLContext;
import javax.net.ssl.X509TrustManager;

import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.util.EntityUtils;

/**
 * @author sunweisheng
 * 请求HTTP和HTTPS协议获得字符串响应结果
 */
public class HttpRequest {

	private final String uri;
	private String statusLine;
	private int statusCode;
	private final String tlsVersion;

	/**
	 * 通过要请求的URI构建请求对象
	 * @param uri 要请求的URI地址
	 */
	public HttpRequest(String uri) {
		this.uri = uri;
		this.tlsVersion = "TLSv1.2";
	}

	/**
	 * 通过URI和TLS版本构建请求对象
	 * @param uri 要请求的URI地址
	 * @param tlsVersion SSL/TLS版本 默认：TLSv1.2
	 */
	public HttpRequest(String uri, String tlsVersion) {
		this.uri = uri;
		this.tlsVersion = "".equals(tlsVersion) ? "TLSv1.2" : tlsVersion;
	}

	/**
	 * 获得请求结果HTTP状态的字符串说明
	 * @return HTTP状态的字符串说明
	 */
	public String getStatusLine() {
		return statusLine;
	}

	/**
	 * 获得请求结果HTTP状态码
	 * @return HTTP状态码
	 */
	public int getStatusCode() {
		return statusCode;
	}

	/**
	 * 根据HTTP或HTTPS创建CloseableHttpClient对象
	 * @return CloseableHttpClient对象
	 * @throws Exception Exception异常
	 */
	private CloseableHttpClient createClientByProtocol() throws Exception {
		final String http = "http";
		final String https = "https";
		String lower = this.uri.toLowerCase();
		if (lower.startsWith(https)) {
			SSLContext sslcontext = SSLContexts.custom().build();
			sslcontext.init(null, new X509TrustManager[] {new HttpsTrustManager()}, new SecureRandom());

			SSLConnectionSocketFactory factory = new SSLConnectionSocketFactory(
					sslcontext,
					new String[] {this.tlsVersion},
					null,
					SSLConnectionSocketFactory.getDefaultHostnameVerifier());

			return HttpClients.custom()
					.setSSLSocketFactory(factory)
					.build();
		}
		else if (lower.startsWith(http)) {
			return HttpClients.createDefault();
		}
		else {
			throw new IllegalArgumentException("URI needs to start with http or https");
		}
	}

	/**
	 * 获得GET请求字符串结果
	 * @return GET请求字符串结果
	 * @throws Exception Exception异常
	 */
	public String get() throws Exception {
		String result = "";
		try (final CloseableHttpClient client = this.createClientByProtocol()) {
			HttpGet httpGet = new HttpGet(this.uri);
			try (final CloseableHttpResponse response = client.execute(httpGet)) {
				this.statusCode = response.getStatusLine().getStatusCode();
				this.statusLine = response.getStatusLine().toString();
				if (this.statusCode == HttpStatus.SC_OK) {
					result = EntityUtils.toString(response.getEntity(), Charset.defaultCharset());
				}
			}
		}

		return result;
	}
}
