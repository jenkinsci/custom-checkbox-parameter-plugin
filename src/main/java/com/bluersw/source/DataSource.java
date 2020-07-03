package com.bluersw.source;

/**
 * 数据源接口 Data source interface
 * @author sunweisheng
 */
public interface DataSource {

	/**
	 * 获取数据 retrieve data
	 * @return 获取数据的字符串结果 Get string result of data
	 * @throws Exception 与IO异常相关 Related to IO exception
	 */
	String get() throws Exception;

	/**
	 * 获取执行结果状态的字符串说明 Get string description of execution result status
	 * @return 执行结果状态的字符串说明 String description of execution result status
	 */
	String getStatusLine();

	/**
	 * 获取执行结果状态码 Get execution result status code
	 * @return 执行结果状态码，成功统一用200表示 Execution result status code, successfully expressed by 200
	 */
	int getStatusCode();
}
