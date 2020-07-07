package com.bluersw.analyze;

import java.io.InputStream;
import java.util.LinkedHashMap;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * 解析JSON格式文档 Parsing JSON format documents
 * @author sunweisheng
 */
public class JsonFormat extends AbstractFormat {

	/**
	 * 构造函数 Constructor
	 * @param jsonContent JSON内容 JSON content
	 * @throws Exception 父类加载数据时或转化结构时可能抛出异常 Exceptions may be thrown when the parent class loads data or transforms structures
	 */
	public JsonFormat(String jsonContent) throws Exception{
		super(jsonContent);
	}

	/**
	 * 构造函数 Constructor
	 * @param inputStream JSON格式文件的流对象 Stream object of JSON format file
	 * @throws Exception 父类加载数据时或转化结构时可能抛出异常 Exceptions may be thrown when the parent class loads data or transforms structures
	 */
	public JsonFormat(InputStream inputStream) throws Exception {
		super(inputStream);
	}

	/**
	 * 加载JSON内容并结构化为LinkedHashMap  Load JSON content and structure it as LinkedHashMap
	 * @return JSON内容的LinkedHashMap结构  LinkedHashMap structure of JSON content
	 * @throws Exception 结构转化时可能会抛出异常 Exceptions may be thrown during structural transformation
	 */
	@SuppressWarnings("unchecked")
	@Override
	protected LinkedHashMap<String, Object> loadData() throws Exception {
		ObjectMapper mapper = new ObjectMapper();
		return mapper.readValue(this.content, LinkedHashMap.class);
	}
}
