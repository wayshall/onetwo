package org.onetwo.common.jsonrpc.protocol;

import java.util.Map;


@SuppressWarnings("serial")
public class NamedParamsRequest extends JsonRpcRequest {

	private Map<String, Object> params;

	public Map<String, Object> getParams() {
		return params;
	}

	public void setParams(Map<String, Object> params) {
		this.params = params;
	}
	
}
