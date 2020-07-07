package com.bluersw.source;

import java.io.IOException;
import java.nio.charset.Charset;

import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

/**
 * 通过HTTP和HTTPS协议获得响应结果 Get response results through HTTP and HTTPS protocols
 * @author sunweisheng
 */
public class HttpRequest implements DataSource {

	private final String uri;
	private String statusLine;
	private int statusCode;

	/**
	 * 通过要请求的URI构建请求对象 Construct the request object by the URI to be requested
	 * @param uri 要请求的URI地址 URL
	 */
	public HttpRequest(String uri) {
		this.uri = uri;
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
	 * 创建CloseableHttpClient对象 Create CloseableHttpClient object
	 * @return CloseableHttpClient对象 CloseableHttpClient
	 * @throws Exception 异常 Exception
	 */
	private CloseableHttpClient createClientByProtocol() throws Exception {
		final String http = "http";
		String lower = this.uri.toLowerCase();
		if (lower.startsWith(http)) {
			return HttpClients.createDefault();
		}else {
			throw new IllegalArgumentException("URI needs to start with http or https");
		}
	}

	/**
	 * 获得GET请求的响应结果 Get the response result of GET request
	 * @return 响应结果 Response results
	 * @throws Exception 异常 Exception
	 */
	@Override
	public String get() throws Exception {
		try (final CloseableHttpClient client = this.createClientByProtocol()) {
			HttpGet httpGet = new HttpGet(this.uri);
			try (final CloseableHttpResponse response = client.execute(httpGet)) {
				this.statusCode = response.getStatusLine().getStatusCode();
				this.statusLine = response.getStatusLine().toString();
				if (this.statusCode == HttpStatus.SC_OK) {
					return EntityUtils.toString(response.getEntity(), Charset.defaultCharset());
				}else {
					throw new IOException("HTTP or HTTPS Get data failed,HttpStatus:" + this.statusLine);
				}
			}
		}
	}
}
