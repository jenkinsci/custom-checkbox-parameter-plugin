package com.bluersw.analyze;

import java.io.InputStream;
import java.util.LinkedHashMap;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author sunweisheng
 */
public class JsonFormat extends AbstractFormat {

	/**
	 * 构造函数
	 * @param jsonContent JSON内容
	 * @throws Exception 父类加载数据时或转化结构时可能抛出异常
	 */
	public JsonFormat(String jsonContent) throws Exception{
		super(jsonContent);
	}

	/**
	 * 构造函数
	 * @param inputStream JSON格式文件的流对象
	 * @throws Exception 父类加载数据时或转化结构时可能抛出异常
	 */
	public JsonFormat(InputStream inputStream) throws Exception {
		super(inputStream);
	}

	/**
	 * 加载JSON内容并结构化为LinkedHashMap<String, Object>
	 * @return JSON内容的LinkedHashMap<String, Object>结构
	 * @throws Exception 结构转化时可能会抛出异常
	 */
	@SuppressWarnings("unchecked")
	@Override
	protected LinkedHashMap<String, Object> loadData() throws Exception {
		ObjectMapper mapper = new ObjectMapper();
		return mapper.readValue(this.content, LinkedHashMap.class);
	}
}
