package com.bluersw.analyze;

import java.util.List;

/**
 * 实现对配置文档操作接口
 * @author sunweisheng
 */
public interface Configuration {

	/**
	 * 根据搜索命令对文件进行搜索并返回结果列表
	 * @param searchCommand 搜索命令，格式类似XPath语法，以"\\"开始每层用"\"分割开
	 * @return 返回搜索结果列表
	 */
	List<String> getValueListBySearch(String searchCommand);
}
