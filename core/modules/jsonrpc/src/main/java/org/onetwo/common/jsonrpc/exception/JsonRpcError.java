package org.onetwo.common.jsonrpc.exception;

import java.util.stream.Stream;

import org.onetwo.common.jsonrpc.protocol.JsonRpcResponseError;

public enum JsonRpcError {
	PARSE_ERROR(-32700, "Parse error", "语法解析错误, 无效的json"),
	INVALID_REQUEST(-32600, "Invalid Request", "发送的json不是一个有效的请求对象"),
	METHOD_NOT_FOUND(-32601, "Method not found", "该方法不存在或无效"),
	INVALID_PARAMS(-32602, "Invalid params", "无效的方法参数"),
	INTERNAL_ERROR(-32603, "Internal error", "服务端错误"),
	//-32000 to -32099
	SERVER_ERROR(-32099, "Server error", "JSON-RPC内部错误"),
	INVALID_RESPONSE(-32000, "Invalid Response", "服务器返回的json不是一个有效的响应对象"),
	HTTP_ERROR(-32001, "http error", "传输协议错误.");
	
	private final int code;
	private final String message;
	private final String meaning;
	private JsonRpcError(int code, String message, String meaning) {
		this.code = code;
		this.message = message;
		this.meaning = meaning;
	}
	public int getCode() {
		return code;
	}
	public String getMessage() {
		return message;
	}
	public String getMeaning() {
		return meaning;
	}
	
	public static JsonRpcError valueOf(JsonRpcResponseError error){
		return Stream.of(values()).filter(e->e.code==error.getCode()).findFirst().get();
	}
}
