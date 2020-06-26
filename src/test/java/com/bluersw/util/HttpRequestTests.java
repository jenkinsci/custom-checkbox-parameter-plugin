package com.bluersw.util;

import org.junit.Test;

import static org.junit.Assert.*;

public class HttpRequestTests {

	@Test
	public void httpGet()throws Exception {
		HttpRequest request = new HttpRequest("http://hc.apache.org/");
		assertNotNull(request.get());
		assertEquals(request.getStatusLine(),"HTTP/1.1 200 OK");
		assertEquals(request.getStatusCode(),200);
	}

	@Test
	public void httpsGet() throws Exception{
		HttpRequest request = new HttpRequest("https://raw.githubusercontent.com/sunweisheng/Jenkins/master/README.md");
		assertNotNull(request.get());
		assertEquals(request.getStatusLine(),"HTTP/1.1 200 OK");
		assertEquals(request.getStatusCode(),200);
	}

	@Test
	public void  TSL_Version() throws Exception{
		HttpRequest request = new HttpRequest("https://raw.githubusercontent.com/sunweisheng/Jenkins/master/README.md","TLSv1");
		assertNotNull(request.get());
		assertEquals(request.getStatusLine(),"HTTP/1.1 200 OK");
		assertEquals(request.getStatusCode(),200);
	}
}