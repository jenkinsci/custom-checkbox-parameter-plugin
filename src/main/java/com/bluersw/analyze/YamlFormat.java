package com.bluersw.analyze;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.yaml.snakeyaml.Yaml;

/**
 * 对YAML格式的内容进行分析和检索内容
 * @author sunweisheng
 */
public class YamlFormat implements Configuration {

	private final String yamlContent;
	private final LinkedHashMap<String,String> index = new LinkedHashMap<>();

	/**
	 * 构造函数
	 * @param yamlContent YAML格式内容
	 */
	public YamlFormat(String yamlContent) {
		this.yamlContent = yamlContent;

		initializeData();
	}

	/**
	 * 构造函数
	 * @param inputStream YAML格式文件的流对象
	 * @throws Exception 可能会抛出和IO相关的异常
	 */
	public YamlFormat(InputStream inputStream) throws Exception {

		try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, Charset.defaultCharset()))) {
			final String lineFeed = "\r\n";
			StringBuilder buffer = new StringBuilder();
			String line;
			while ((line = reader.readLine()) != null) {
				buffer.append(line).append(lineFeed);
			}

			this.yamlContent = buffer.toString();
		}

		initializeData();
	}

	/**
	 * 加载YAML数据并建立数据索引
	 */
	@SuppressWarnings("unchecked")
	private void initializeData() {
		Yaml yaml = new Yaml();
		LinkedHashMap<String,Object> data = yaml.loadAs(this.yamlContent,LinkedHashMap.class);

		initializeIndex(data,"//");
	}

	/**
	 * 建立YAML数据的索引列表
	 * @param object 递归算法使用，表示子节点对象
	 * @param parentName 记录父节点全部路并径用"/"分割
	 */
	@SuppressWarnings("rawtypes")
	private void initializeIndex(Object object, String parentName){
		Iterator iterator = null;
		parentName = parentName.endsWith("/") ? parentName : parentName+"/";
		if(object instanceof LinkedHashMap){
			LinkedHashMap linkMap = (LinkedHashMap)object;
			iterator = linkMap.entrySet().iterator();
		}else if(object instanceof ArrayList){
			ArrayList list = (ArrayList)object;
			iterator = list.iterator();
		}else if(object instanceof Map.Entry) {
			Map.Entry entry = (Map.Entry) object;
			if (entry.getValue() instanceof ArrayList || entry.getValue() instanceof LinkedHashMap) {
				parentName = parentName + entry.getKey().toString() + "/";
				initializeIndex(entry.getValue(), parentName);
			}
			else {
				parentName = parentName + entry.getKey().toString();
				parentName = parentName + "@" + (parentName + "/" + entry.getValue().toString()).hashCode();
				this.index.put(parentName, entry.getValue().toString());
			}
		}

		if (iterator != null){
			while(iterator.hasNext()){
				initializeIndex(iterator.next(),parentName);
			}
		}
	}

	/**
	 * 通过搜索命令检索YAML中的数据并返回结果列表
	 * @param searchCommand 搜索命令，格式类似XPath语法，以"\\"开始每层用"\"分割开
	 * @return 对YAML搜索结果列表
	 */
	@Override
	public List<String> getValueListBySearch(String searchCommand){
		List<String> result = new ArrayList<>();
		for (Map.Entry<String, String> entry : this.index.entrySet()) {
			if (entry.getKey().startsWith(searchCommand)) {
				result.add(entry.getValue());
			}
		}
		return result;
	}
}
