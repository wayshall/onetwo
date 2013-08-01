package org.onetwo.common.rest.vo;

import java.io.Serializable;

@SuppressWarnings("serial")
public class MdmResult implements Serializable {

	public static final int SUCCEED = 1;
	public static final int FAILED = 0;
	
	private int code = SUCCEED;
	private String message;
	
	public boolean isSucceed(){
		return code == SUCCEED;
	}
	public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	
	public String toString(){
		return "{code:"+code+", message:"+message+"}";
	}
	
}
