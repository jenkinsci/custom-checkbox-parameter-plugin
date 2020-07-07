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

/**
 * 分析文件内容抽象类 Analysis file content abstract class
 * @author sunweisheng
 */
public abstract class AbstractFormat implements Configuration {
	protected final String content;
	protected final LinkedHashMap<String,String> index = new LinkedHashMap<>();
	protected LinkedHashMap<String,Object> data;

	/**
	 * 构造函数 Constructor
	 * @param content 文件内容 document content
	 * @throws Exception 结构化数据时可产生异常 Exceptions can occur when structuring data
	 */
	public AbstractFormat(String content) throws Exception{
		this.content = content;
		initializeData();
	}

	/**
	 * 构造函数 Constructor
	 * @param inputStream 文件的输入流对象 File input stream object
	 * @throws Exception 读取流数据或结构化数据时可产生异常 Exceptions can occur when reading streaming data or structured data
	 */
	public AbstractFormat(InputStream inputStream) throws Exception {
		try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, Charset.defaultCharset()))) {
			final String lineFeed = "\r\n";
			StringBuilder buffer = new StringBuilder();
			String line;
			while ((line = reader.readLine()) != null) {
				buffer.append(line).append(lineFeed);
			}

			this.content = buffer.toString();
		}

		initializeData();
	}

	/**
	 * 初始化数据（加载数据、建立索引数据） Initialize data (load data, establish index data)
	 * @throws Exception 读取数据或结构化数据时可产生异常 Exceptions can occur when reading data or structured data
	 */
	private void initializeData() throws Exception{
		this.data = loadData();
		initializeIndex(this.data,"//");
	}

	/**
	 * 加载文件中的数据，并转化LinkedHashMap结构 Load the data in the file and convert LinkedHashMap structure
	 * @throws Exception 读取数据或结构化数据时可产生异常 Exceptions can occur when reading data or structured data
	 * @return LinkedHashMap 结构数据 LinkedHashMap structure data
	 */
	protected abstract LinkedHashMap<String,Object> loadData() throws Exception;

	/**
	 * 初始化索引数据 Initialize index data
	 * @param object 递归算法使用，表示子节点对象 Recursive algorithm used to represent child node objects
	 * @param parentName 记录父节点全部路并径用"/"分割，顶级用"//"表示 Record all paths of the parent node and use "/" to split, the top level is represented by "//"
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
				//允许重复的Key但不同的Value，Key+Value是唯一的  Allow duplicate Key but different Value, Key+Value is unique
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
	 * 实现Configuration接口，通过搜索命令检索索引中的数据并返回结果列表 Implement the Configuration interface, retrieve the data in the index through the search command and return the result list
	 * @param searchCommand 搜索命令，格式类似XPath语法，以"//"开始每层用"/"分割开 Search command, the format is similar to XPath syntax, starting with "//" and separating each layer with "/"
	 * @return 搜索结果列表 Search result list
	 */
	@Override
	public List<String> getValueListBySearch(String searchCommand){
		searchCommand = searchCommand.replace('\\','/');
		List<String> result = new ArrayList<>();
		for (Map.Entry<String, String> entry : this.index.entrySet()) {
			if (entry.getKey().startsWith(searchCommand)) {
				result.add(entry.getValue());
			}
		}
		return result;
	}
}
