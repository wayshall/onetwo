package org.onetwo.plugins.permission;

import org.springframework.web.bind.annotation.RequestMethod;

public class RequestInfo {

	private final String uri;
	private final RequestMethod method;

	public RequestInfo(String uri, RequestMethod method) {
		super();
		this.uri = uri;
		this.method = method;
	}

	public String getUri() {
		return uri;
	}

	public RequestMethod getMethod() {
		return method;
	}
	
	public String toString(){
		return method.toString()+":"+uri;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + method.hashCode();
		result = prime * result + uri.hashCode();
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		RequestInfo other = (RequestInfo) obj;
		if (method != other.method)
			return false;
		return uri.equals(other.uri);
	}

}
