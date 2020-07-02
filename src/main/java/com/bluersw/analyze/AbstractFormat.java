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
 * 分析配置文件抽象类
 * @author sunweisheng
 */
public abstract class AbstractFormat implements Configuration {
	protected final String content;
	protected final LinkedHashMap<String,String> index = new LinkedHashMap<>();
	protected LinkedHashMap<String,Object> data;

	/**
	 * 构造函数
	 * @param content 文件内容
	 * @throws Exception 结构化数据时可产生异常
	 */
	public AbstractFormat(String content) throws Exception{
		this.content = content;
		initializeData();
	}

	/**
	 * 构造函数
	 * @param inputStream 文件的输入流对象
	 * @throws Exception 读取流数据或结构化数据时可产生异常
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
	 * 初始化数据（加载数据、建立索引数据）
	 * @throws Exception 读取数据或结构化数据时可产生异常
	 */
	private void initializeData() throws Exception{
		this.data = loadData();
		initializeIndex(this.data,"//");
	}

	/**
	 * 加载文件中的数据，并转化LinkedHashMap<String,Object>结构
	 * @throws Exception 读取数据或结构化数据时可产生异常
	 * @return LinkedHashMap<String,Object>结构数据
	 */
	protected abstract LinkedHashMap<String,Object> loadData() throws Exception;

	/**
	 * 初始化索引数据
	 * @param object 递归算法使用，表示子节点对象
	 * @param parentName 记录父节点全部路并径用"/"分割，顶级用"//"表示
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
	 * 实现Configuration接口，通过搜索命令检索索引中的数据并返回结果列表
	 * @param searchCommand 搜索命令，格式类似XPath语法，以"//"开始每层用"/"分割开
	 * @return 搜索结果列表
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
