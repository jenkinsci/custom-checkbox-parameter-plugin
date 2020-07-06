package com.bluersw.source;

/**
 * 数据源协议枚举 Data source protocol enumeration
 * @author sunweisheng
 */

public enum Protocol {
	/**
	 * 通过HTTP或HTTPS协议获取数据 Get data via HTTP or HTTPS protocol
	 */
	HTTP_HTTPS,
	/**
	 * 通过文件路径获取数据 Get data by file path
	 */
	FILE_PATH
}
