package com.bluersw.format;

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
 * @author sunweisheng
 */
public class YamlFormat {

	private String searchCommand;
	private String yamlContent;
	private LinkedHashMap<String,Object> data;
	private final LinkedHashMap<String,String> index = new LinkedHashMap<>();

	private YamlFormat(String searchCommand) {
		setSearchCommand(searchCommand);
	}

	public YamlFormat(String searchCommand, String yamlContent) {
		this(searchCommand);
		this.yamlContent = yamlContent;

		initializeData();
	}

	public YamlFormat(String searchCommand, InputStream inputStream) throws Exception {
		this(searchCommand);

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

	@SuppressWarnings("unchecked")
	private void initializeData() {
		Yaml yaml = new Yaml();
		this.data = yaml.loadAs(this.yamlContent,LinkedHashMap.class);

		initializeIndex(this.data,"//");
	}

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

	public void setSearchCommand(String searchCommand){
		this.searchCommand = searchCommand;
	}

	public List<String> getValueListBySearch(){
		List<String> result = new ArrayList<>();
		for (Map.Entry<String, String> entry : this.index.entrySet()) {
			if (entry.getKey().startsWith(this.searchCommand)) {
				result.add(entry.getValue());
			}
		}
		return result;
	}
}
