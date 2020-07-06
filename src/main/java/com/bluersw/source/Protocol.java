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
	 * 通过Jenkins服务器的文件系统获取数据 Get data through the file system of the Jenkins server
	 */
	JENKINS_LOCAL
}
