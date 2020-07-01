package com.bluersw.analyze;

import java.io.InputStream;

/**
 * 配置文件操作实现工厂
 * @author sunweisheng
 */
public class ConfigurationFactory {

	/**
	 * 根据文档类型和内容创建配置文件操作对象
	 * @param format 文档类型
	 * @param content 文档内容
	 * @return 配置文件操作对象
	 */
	public static Configuration createConfiguration(Format format, String content) throws Exception{
		if(format == Format.YAML){
			return new YamlFormat(content);
		}else if (format == Format.JSON){
			return new JsonFormat(content);
		}else if (format == Format.Empty){
			return new EmptyFormat(content);
		}else{
			throw new IllegalArgumentException("There is no implementation of this document format.");
		}
	}

	/**
	 * 根据文档类型和流对象创建配置文件操作对象
	 * @param format 文档类型
	 * @param inputStream 文档内容流对象
	 * @return 配置文件操作对象
	 * @throws Exception 异常
	 */
	public static Configuration createConfiguration(Format format, InputStream inputStream) throws Exception{
		if(format == Format.YAML){
			return new YamlFormat(inputStream);
		}else if (format == Format.JSON){
			return new JsonFormat(inputStream);
		}else if (format == Format.Empty){
			return new EmptyFormat(inputStream);
		}else{
			throw new IllegalArgumentException("There is no implementation of this document format.");
		}
	}
}
