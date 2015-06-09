package org.onetwo.common.jsonrpc.protocol;

import java.io.Serializable;

import org.onetwo.common.jsonrpc.exception.JsonRpcError;

@SuppressWarnings("serial")
public class JsonRpcResponseError implements Serializable {
	
	public static JsonRpcResponseError create(JsonRpcError rpcError, String detailMsg){
		JsonRpcResponseError error = new JsonRpcResponseError();
		error.setCode(rpcError.getCode());
		error.setMessage(rpcError.getMessage());
		error.setData(rpcError.getMeaning()+" "+detailMsg);
		return error;
	}
	
	/***
	 * 使用数值表示该异常的错误类型。 必须为整数。
	 */
	private int code;
	/***
	 * 对该错误的简单描述字符串。 该描述应尽量限定在简短的一句话。
	 */
	private String message;
	/***
	 * 包含关于错误附加信息的基本类型或结构化类型。该成员可忽略。 该成员值由服务端定义（例如详细的错误信息，嵌套的错误等）。
	 */
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
