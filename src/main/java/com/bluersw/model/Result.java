package com.bluersw.model;

/**
 * @author sunweisheng
 */
public class Result<T>  {

	private boolean succeed =false;
	private String message;
	private T content;

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public boolean isSucceed() {
		return succeed;
	}

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
