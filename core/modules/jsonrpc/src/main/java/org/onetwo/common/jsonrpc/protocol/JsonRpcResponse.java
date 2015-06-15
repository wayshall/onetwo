package org.onetwo.common.jsonrpc.protocol;

@SuppressWarnings("serial")
public class JsonRpcResponse extends JsonRpcBase<Long>{
	
	/***
	 * 该成员在成功时必须包含。
		当调用方法引起错误时必须不包含该成员。
		服务端中的被调用方法决定了该成员的值。
	 */
	private Object result;
	/***
	 * 该成员在失败是必须包含。
		当没有引起错误的时必须不包含该成员。
		该成员参数值必须为5.1中定义的对象。
	 */
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
