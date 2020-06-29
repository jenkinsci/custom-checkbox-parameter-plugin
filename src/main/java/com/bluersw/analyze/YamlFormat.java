package com.bluersw.analyze;

import java.io.InputStream;
import java.util.LinkedHashMap;

import org.yaml.snakeyaml.Yaml;

/**
 * 对YAML格式的内容进行分析和检索内容
 * @author sunweisheng
 */
public class YamlFormat extends AbstractFormat {

	/**
	 * 构造函数
	 * @param yamlContent YAML格式内容
	 * @throws Exception 父类加载数据时或转化结构时可能抛出异常
	 */
	public YamlFormat(String yamlContent) throws Exception {
		super(yamlContent);
	}

	/**
	 * 构造函数
	 * @param inputStream YAML格式文件的流对象
	 * @throws Exception 父类加载数据时或转化结构时可能抛出异常
	 */
	public YamlFormat(InputStream inputStream) throws Exception {
		super(inputStream);
	}

	/**
	 * 加载YAML内容并结构化为LinkedHashMap<String, Object>
	 * @return YAML内容的LinkedHashMap<String, Object>结构
	 */
	@SuppressWarnings("unchecked")
	@Override
	protected LinkedHashMap<String, Object> loadData() {
		Yaml yaml = new Yaml();
		return yaml.loadAs(this.content,LinkedHashMap.class);
	}
}
