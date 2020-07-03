package com.bluersw.source;

import org.junit.Test;

import static org.junit.Assert.*;

public class HttpRequestTests {

	@Test
	public void  httpGet() throws Exception{
		HttpRequest request = new HttpRequest("https://raw.githubusercontent.com/sunweisheng/Jenkins/master/examples/example.yaml");
		assertNotNull(request.get());
		assertEquals(request.getStatusLine(),"HTTP/1.1 200 OK");
		assertEquals(request.getStatusCode(),200);
	}
}