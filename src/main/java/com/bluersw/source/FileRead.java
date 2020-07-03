package com.bluersw.source;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

/**
 * 文件读取类 File reading class
 * @author sunweisheng
 */
public class FileRead implements DataSource {
	private final String filePath;
	private String statusLine;
	private int statusCode;
	private static final String FILE_SEPARATOR = System.getProperty("file.separator");

	/**
	 * 构建函数 Constructor
	 * @param filePath 文件路径 file path
	 */
	public FileRead(String filePath){
		this.filePath = filePath.replace("/",FILE_SEPARATOR).replace("\\",FILE_SEPARATOR);
		this.statusLine = "unknown.StatusCode:0";
		this.statusCode = 0;
	}

	@Override
	public String get() throws Exception {
		InputStream inputStream = new FileInputStream(this.filePath);
		try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, Charset.defaultCharset()))) {
			final String lineFeed = "\r\n";
			StringBuilder buffer = new StringBuilder();
			String line;
			while ((line = reader.readLine()) != null) {
				buffer.append(line).append(lineFeed);
			}

			this.statusLine = "Read File Success.StatusCode:200";
			this.statusCode = 200;
			return buffer.toString();
		}
	}

	@Override
	public String getStatusLine() {
		return this.statusLine;
	}

	@Override
	public int getStatusCode() {
		return this.statusCode;
	}
}
