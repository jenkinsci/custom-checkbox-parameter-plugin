package com.bluersw.analyze;

import java.util.List;

/**
 * 文档内容操作接口 Document content operation interface
 * @author sunweisheng
 */
public interface Configuration {

	/**
	 * 根据搜索命令对文件进行搜索并返回结果列表 Search the file according to the search command and return a list of results
	 * @param searchCommand 搜索命令，格式类似XPath语法，以"//"开始每层用"/"分割开 Search command, the format is similar to XPath syntax, starting with "//" and separating each layer with "/"
	 * @return 返回搜索结果列表 Back to search results list
	 */
	List<String> getValueListBySearch(String searchCommand);
}
