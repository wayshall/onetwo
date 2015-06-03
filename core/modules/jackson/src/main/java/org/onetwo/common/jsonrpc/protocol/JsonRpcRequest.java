package org.onetwo.common.jsonrpc.protocol;


public class JsonRpcRequest extends JsonRpcBase<Long> {

	private String method;

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}
	
}
