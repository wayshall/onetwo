package org.onetwo.common.jsonrpc.protocol;

import java.io.Serializable;

@SuppressWarnings("serial")
public class JsonRpcBase<T> implements Serializable{
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
