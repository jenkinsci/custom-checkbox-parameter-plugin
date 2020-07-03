package com.bluersw.source;

/**
 * 数据源操作协议枚举
 * @author sunweisheng
 */

public enum Protocol {
	/**
	 * 通过HTTP协议获取数据
	 */
	HTTP,
	/**
	 * 通过HTTPS协议获取数据
	 */
	HTTPS,
	/**
	 * 通过Jenkins服务器的文件系统获取数据
	 */
	JENKINS_LOCAL
}
