package org.onetwo.common.ws;

public interface WSBlock<T> {
	
	public Object getWebServiceImpl();

	public T doExecute(T wsresult) throws Exception;
	public void doExcepted(T wsresult);
	public void doFinally(T wsresult);
	
}
