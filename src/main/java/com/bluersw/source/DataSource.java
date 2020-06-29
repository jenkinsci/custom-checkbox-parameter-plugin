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

	/**
	 * 获取执行结果状态的字符串说明
	 * @return 执行结果状态的字符串说明
	 */
	String getStatusLine();

	/**
	 * 获取执行结果状态码
	 * @return 执行结果状态码
	 */
	int getStatusCode();
}
