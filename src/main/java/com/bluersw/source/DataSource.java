package com.bluersw.source;

/**
 * 数据源接口
 * @author sunweisheng
 */
public interface DataSource {

	/**
	 * 获取数据
	 * @return 获取数据的字符串结果
	 * @throws Exception 与IO异常相关
	 */
	String get() throws Exception;
}
