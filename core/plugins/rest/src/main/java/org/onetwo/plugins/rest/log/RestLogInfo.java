package org.onetwo.plugins.rest.log;

import java.util.Map;

public class RestLogInfo {
	private String url;
	private Map<?, ?> params;
	private Object result;
	
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public Map<?, ?> getParams() {
		return params;
	}
	public void setParams(Map<?, ?> params) {
		this.params = params;
	}
	public Object getResult() {
		return result;
	}
	public void setResult(Object result) {
		this.result = result;
	}
}
