package com.bluersw.model;

/**
 * 方法通用返回数据类型 Method common return data type
 * @author sunweisheng
 */
public class Result<T>  {

	private boolean succeed =false;
	private String message;
	private T content;

	/**
	 * 方法关于执行结果的说明 Description of the method's execution results
	 * @return 结果说明 Explanation of results
	 */
	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	/**
	 * 方法操作的结果是否成功 Whether the result of the method operation is successful
	 * @return 是否成功 whether succeed
	 */
	public boolean isSucceed() {
		return succeed;
	}

	/**
	 * 方法返回的数据 The data returned by the method
	 * @return 数据 data
	 */
	public T getContent() {
		return content;
	}

	public void setSucceed(boolean succeed) {
		this.succeed = succeed;
	}

	public void setContent(T content) {
		this.content = content;
	}
}
