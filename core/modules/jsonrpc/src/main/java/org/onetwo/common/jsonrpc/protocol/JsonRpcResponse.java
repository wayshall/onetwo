package org.onetwo.common.jsonrpc.protocol;

@SuppressWarnings("serial")
public class JsonRpcResponse extends JsonRpcBase<Long>{
	
	private Object result;
	private JsonRpcResponseError error;
	public Object getResult() {
		return result;
	}
	public void setResult(Object result) {
		this.result = result;
	}
	public JsonRpcResponseError getError() {
		return error;
	}
	public void setError(JsonRpcResponseError error) {
		this.error = error;
	}

}
