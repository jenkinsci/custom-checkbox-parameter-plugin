package com.bluersw.source;

/**
 * 数据源工厂类
 * @author sunweisheng
 */
public class DataSourceFactory {

	/**
	 * 创建数据源接口
	 * @param protocol 协议
	 * @param uri URI定位
	 * @return 数据源接口对象
	 */
	public static DataSource createDataSource(Protocol protocol, String uri) {
		if (protocol == Protocol.HTTP) {
			return new HttpRequest(uri);
		}else if(protocol == Protocol.LOCAL){
			return new FileRead(uri);
		}else {
			throw new IllegalArgumentException("Did not implement this protocol.");
		}
	}

	/**
	 * 创建数据源接口
	 * @param protocol 协议
	 * @param uri URI定位
	 * @param protocolVersion 协议版本（可空）
	 * @return 数据源接口对象
	 */
	public static DataSource createDataSource(Protocol protocol, String uri, String protocolVersion) {
		if (protocol == Protocol.HTTP) {
			return new HttpRequest(uri, protocolVersion);
		}else if(protocol == Protocol.LOCAL){
			return new FileRead(uri);
		}else {
			throw new IllegalArgumentException("Did not implement this protocol.");
		}
	}
}
