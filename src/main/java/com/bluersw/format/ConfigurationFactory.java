package com.bluersw.format;

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
	public static Configuration createConfiguration(Format format, String content){
		if(format == Format.YAML){
			return new YamlFormat(content);
		}else {
			throw new IllegalArgumentException("There is no implementation of this document format.");
		}
	}
}
