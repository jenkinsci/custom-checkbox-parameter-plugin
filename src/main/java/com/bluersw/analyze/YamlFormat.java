package com.bluersw.analyze;

import java.io.InputStream;
import java.util.LinkedHashMap;

import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.SafeConstructor;

/**
 * 解析YAML格式的内容 Parse content in YAML format
 * @author sunweisheng
 */
public class YamlFormat extends AbstractFormat {

	/**
	 * 构造函数 Constructor
	 * @param yamlContent YAML格式内容 YAML format content
	 * @throws Exception 父类加载数据时或转化结构时可能抛出异常 Exceptions may be thrown when the parent class loads data or transforms structures
	 */
	public YamlFormat(String yamlContent) throws Exception {
		super(yamlContent);
	}

	/**
	 * 构造函数 Constructor
	 * @param inputStream YAML格式文件的流对象 Stream object of YAML format file
	 * @throws Exception 父类加载数据时或转化结构时可能抛出异常 Exceptions may be thrown when the parent class loads data or transforms structures
	 */
	public YamlFormat(InputStream inputStream) throws Exception {
		super(inputStream);
	}

	/**
	 * 加载YAML内容并结构化为LinkedHashMap Load YAML content and structure it as LinkedHashMap
	 * @return YAML内容的LinkedHashMap结构  LinkedHashMap structure of YAML content
	 */
	@Override
	protected LinkedHashMap<String, Object> loadData() {
		//using safe constructor
		Yaml yaml = new Yaml(new SafeConstructor());
		return yaml.load(this.content);
	}
}
