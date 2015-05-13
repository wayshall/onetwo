package org.onetwo.plugins.rest;

import org.onetwo.common.utils.Result;

@SuppressWarnings("serial")
public class BaseRestResult<T> implements Result<String, T> {

	private String code;
	private String message;
	private T data;
	
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public T getData() {
		return data;
	}
	public void setData(T data) {
		this.data = data;
	}
	

}
