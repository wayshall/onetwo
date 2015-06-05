package org.onetwo.common.jsonrpc.protocol;

import java.io.Serializable;

@SuppressWarnings("serial")
abstract public class JsonRpcBase<T> implements Serializable{
	public static class KeyWords {
		//request
		public static final String ID = "id";
		public static final String METHOD = "method";
		public static final String JSONRPC = "jsonrpc";	
		public static final String PARAMS = "params";	
		
		//response
		public static final String RESULT = "result";	
		public static final String ERROR = "error";	
	}
	
	public static final String VERSION = "2.0";
	
	private String jsonrpc = VERSION;
	/***
	 * request:
	 * 已建立客户端的唯一标识id，值必须包含一个字符串、数值或NULL空值。如果不包含该成员则被认定为是一个通知。该值一般不为NULL[1]，若为数值则不应该包含小数[2]。
	 * 
	 * response:
	 * 该成员必须包含。该成员值必须于请求对象中的id成员值一致。若在检查请求对象id时错误（例如参数错误或无效请求），则该值必须为空值。
	 */
	private T id;
	
	public JsonRpcBase() {
		super();
	}
	public String getJsonrpc() {
		return jsonrpc;
	}
	public void setJsonrpc(String jsonrpc) {
		this.jsonrpc = jsonrpc;
	}
	public T getId() {
		return id;
	}
	public void setId(T id) {
		this.id = id;
	}
	
}
