package org.onetwo.common.data;

@SuppressWarnings("serial")
public class SimpleResult implements Result {
	
	private String code;
	
	private String message;

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

	@Override
	public String toString() {
		return "SimpleResult [code=" + code + ", message=" + message + "]";
	}
	
}
