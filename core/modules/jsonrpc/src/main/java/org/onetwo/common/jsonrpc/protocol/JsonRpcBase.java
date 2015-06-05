package org.onetwo.common.jsonrpc.protocol;

import java.io.Serializable;

@SuppressWarnings("serial")
abstract public class JsonRpcBase<T> implements Serializable{
	public static class KeyWords {
		public static final String ID = "id";
		public static final String METHOD = "method";
		public static final String JSONRPC = "jsonrpc";	
		public static final String PARAMS = "params";	
	}
	
	public static final String VERSION = "2.0";
	
	private String jsonrpc = VERSION;
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
