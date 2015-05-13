package org.onetwo.plugins.rest;


@SuppressWarnings("serial")
public class RestResult<T> extends BaseRestResult {

	private T data;
	
	public RestResult(){
		super();
	}

	public T getData() {
		return data;
	}


}
