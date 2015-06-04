package org.onetwo.common.jsonrpc.protocol;

import org.onetwo.common.utils.Assert;


@SuppressWarnings("serial")
public class JsonRpcParamsRequest extends JsonRpcBase<Long> {

	private String method;
	private Object params;

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public Object getParams() {
		return params;
	}
	
	public <T> T getParams(Class<T> clazz){
		Assert.notNull(clazz);
		return clazz.cast(params);
	}

	public void setParams(Object params) {
		this.params = params;
	}
	
}
