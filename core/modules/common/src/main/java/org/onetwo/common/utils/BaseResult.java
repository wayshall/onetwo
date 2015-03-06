package org.onetwo.common.utils;

public class BaseResult<CODE, DATA> implements Result<CODE, DATA> {
	
	private CODE code;
	private String message;
	private DATA data;
	
	public CODE getCode() {
		return code;
	}

	public void setCode(CODE code) {
		this.code = code;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public DATA getData() {
		return data;
	}
	public void setData(DATA data) {
		this.data = data;
	}
	
	

}
