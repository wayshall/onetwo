package org.onetwo.common.jsonrpc.protocol;



@SuppressWarnings("serial")
public class JsonRpcRequest extends JsonRpcBase<Long> {

	private String method;
	private Object params;

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public <T> T getParams(Class<T> clazz) {
		return clazz.cast(params);
	}

	public Object getParams() {
		return params;
	}

	public void setParams(Object params) {
		this.params = params;
	}


	
}
