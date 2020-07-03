package com.bluersw.analyze;

import java.io.InputStream;
import java.util.LinkedHashMap;

/**
 * 如果URI和用户输入内容都是空的情况下创建空文档对象 Create empty document object if both URI and user input are empty
 * @author sunweisheng
 */
public class EmptyFormat extends AbstractFormat {

	public EmptyFormat(String emptyContent) throws Exception{
		super(emptyContent);
	}

	public EmptyFormat(InputStream inputStream) throws Exception {
		super(inputStream);
	}

	@Override
	protected LinkedHashMap<String, Object> loadData() {
		return new LinkedHashMap<>();
	}
}
