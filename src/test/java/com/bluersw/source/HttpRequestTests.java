package com.bluersw.source;

import org.junit.Test;

import static org.junit.Assert.*;

public class HttpRequestTests {

	@Test
	public void  httpsGet_GitHub() throws Exception{
		HttpRequest request = new HttpRequest("https://raw.githubusercontent.com/sunweisheng/Jenkins/master/examples/example.yaml");
		assertNotNull(request.get());
		assertEquals(request.getStatusLine(),"HTTP/1.1 200 OK");
		assertEquals(request.getStatusCode(),200);
	}

	@Test
	public void  httpsGet_Jenkins() throws Exception{
		HttpRequest request = new HttpRequest("https://www.jenkins.io/zh/");
		assertNotNull(request.get());
		assertEquals(request.getStatusLine(),"HTTP/1.1 200 OK");
		assertEquals(request.getStatusCode(),200);
	}
}