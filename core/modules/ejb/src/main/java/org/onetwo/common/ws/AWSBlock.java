package org.onetwo.common.ws;

abstract public class AWSBlock<T> implements WSBlock<T> {

	private Object webServiceImpl;
	
	public AWSBlock(Object webServiceImpl) {
		super();
		this.webServiceImpl = webServiceImpl;
	}

	@Override
	public Object getWebServiceImpl() {
		return webServiceImpl;
	}
	
	@Override
	public T doExecute(T wsresult) throws Exception {
		return execute(wsresult);
	}

	abstract public T execute(T wsresult) throws Exception;

	@Override
	public void doExcepted(T wsresult){
	}

	@Override
	public void doFinally(T wsresult){
	}

	
}
