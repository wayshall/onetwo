package org.onetwo.common.jsonrpc.protocol;

import java.io.Serializable;

import org.onetwo.common.jsonrpc.exception.JsonRpcError;

public class JsonRpcResponseError implements Serializable {
	
	public static JsonRpcResponseError create(JsonRpcError rpcError){
		JsonRpcResponseError error = new JsonRpcResponseError();
		error.setCode(rpcError.getCode());
		error.setMessage(rpcError.getMessage());
		error.setData(rpcError.getMeaning());
		return error;
	}
	
	private int code;
	private String message;
	private Object data;
	
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
	public Object getData() {
		return data;
	}
	public void setData(Object data) {
		this.data = data;
	}
	
}
