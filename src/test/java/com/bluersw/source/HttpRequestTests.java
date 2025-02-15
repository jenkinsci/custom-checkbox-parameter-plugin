package com.bluersw.source;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class HttpRequestTests {

	@Test
	public void  httpsGet_Baidu() throws Exception{
		HttpRequest request = new HttpRequest("https://www.baidu.com");
		assertNotNull(request.get());
		assertEquals(request.getStatusLine(),"HTTP/1.1 200 OK");
		assertEquals(request.getStatusCode(),200);
	}
}