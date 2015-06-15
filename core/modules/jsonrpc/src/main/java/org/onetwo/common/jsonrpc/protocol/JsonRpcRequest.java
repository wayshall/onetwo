package org.onetwo.common.jsonrpc.protocol;



@SuppressWarnings("serial")
public class JsonRpcRequest extends JsonRpcBase<Long> {

	/***
	 * 包含所要调用方法名称的字符串，以rpc开头的方法名，用英文句号（U+002E or ASCII 46）连接的为预留给rpc内部的方法名及扩展名，且不能在其他地方使用。
	 */
	private String method;
	/***
	 * 调用方法所需要的结构化参数值，该成员参数可以被省略。
	 */
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
