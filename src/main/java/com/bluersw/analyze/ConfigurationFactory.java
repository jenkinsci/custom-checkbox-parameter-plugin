package com.bluersw.analyze;

import java.io.InputStream;

/**
 * 文件内容操作对象工厂 File content operation object factory
 * @author sunweisheng
 */
public class ConfigurationFactory {

	/**
	 * 根据文档类型和内容创建文件内容操作对象 Create file content operation object according to document type and content
	 * @param format 文档类型 Document type
	 * @param content 文档内容 Document content
	 * @return 文件内容操作对象 File content operation object
	 */
	public static Configuration createConfiguration(Format format, String content) throws Exception{
		if(format == Format.YAML){
			return new YamlFormat(content);
		}else if (format == Format.JSON){
			return new JsonFormat(content);
		}else if (format == Format.Empty){
			return new EmptyFormat(content);
		}else{
			throw new IllegalArgumentException(String.format("There is no implementation of this document format. format：%s",format));
		}
	}

	/**
	 * 根据文档类型和流对象创建文件内容操作对象 Create file content operation objects based on document types and stream objects
	 * @param format 文档类型 Document type
	 * @param inputStream 文档内容流对象 Document content stream object
	 * @return 文件内容操作对象 File content operation object
	 * @throws Exception 异常 Exception
	 */
	public static Configuration createConfiguration(Format format, InputStream inputStream) throws Exception{
		if(format == Format.YAML){
			return new YamlFormat(inputStream);
		}else if (format == Format.JSON){
			return new JsonFormat(inputStream);
		}else if (format == Format.Empty){
			return new EmptyFormat(inputStream);
		}else{
			throw new IllegalArgumentException(String.format("There is no implementation of this document format. format：%s",format));
		}
	}
}
