package com.bluersw.source;

/**
 * 数据源对象工厂类 Data source object factory class
 * @author sunweisheng
 */
public class DataSourceFactory {

	/**
	 * 创建数据源对象 Create a data source object
	 * @param protocol 协议 protocol
	 * @param uri URI定位 URI
	 * @return 数据源对象 Data source object
	 */
	public static DataSource createDataSource(Protocol protocol, String uri) {
		if (protocol == Protocol.HTTP || protocol == Protocol.HTTPS) {
			return new HttpRequest(uri);
		}else if(protocol == Protocol.JENKINS_LOCAL){
			return new FileRead(uri);
		}else {
			throw new IllegalArgumentException(String.format("Did not implement this protocol. protocol: %s",protocol));
		}
	}

	/**
	 * 创建数据源对象 Create a data source object
	 * @param protocol 协议 protocol
	 * @param uri URI定位 URI
	 * @param protocolVersion 协议版本 Protocol version
	 * @return 数据源对象 Data source object
	 */
	public static DataSource createDataSource(Protocol protocol, String uri, String protocolVersion) {
		if (protocol == Protocol.HTTP || protocol == Protocol.HTTPS) {
			return new HttpRequest(uri, protocolVersion);
		}else if(protocol == Protocol.JENKINS_LOCAL){
			return new FileRead(uri);
		}else {
			throw new IllegalArgumentException(String.format("Did not implement this protocol. protocol: %s",protocol));
		}
	}
}
