package com.bluersw.source;

import java.util.Properties;

import org.junit.Test;

import static org.junit.Assert.*;

public class FileReadTests {

	@Test
	public void get() throws Exception {
		Properties properties = System.getProperties();
		String path = properties.getProperty("user.dir");
		DataSource file = DataSourceFactory.createDataSource(Protocol.FILE_PATH, path + "/src/main/resources/test/analyze/examples.json");
		String json = file.get();
		assertNotNull(json);
		assertEquals(file.getStatusLine(),"Read File Success.StatusCode:200");
		assertEquals(file.getStatusCode(),200);
	}
}