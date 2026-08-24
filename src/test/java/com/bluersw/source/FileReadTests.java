package com.bluersw.source;

import java.util.Properties;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

class FileReadTests {

	@Test
	void get() throws Exception {
		Properties properties = System.getProperties();
		String path = properties.getProperty("user.dir");
		DataSource file = DataSourceFactory.createDataSource(Protocol.FILE_PATH, path + "/src/main/resources/test/analyze/examples.json");
		String json = file.get();
		assertNotNull(json);
		assertEquals("Read File Success.StatusCode:200", file.getStatusLine());
		assertEquals(200, file.getStatusCode());
	}
}