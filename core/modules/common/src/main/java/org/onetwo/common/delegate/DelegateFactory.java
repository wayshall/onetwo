package org.onetwo.common.delegate;

final public class DelegateFactory {

	public static DelegateMethodImpl create(Object delegate, String method, Class<?>...argTypes){
		DelegateMethodImpl delegateObject = new DelegateMethodImpl(delegate, method, argTypes);
		return delegateObject;
	}

	public static MutiDelegate define(Class<?>...argTypes){
		MutiDelegate delegate = new MutiDelegate(argTypes);
		return delegate;
	}
	
	private DelegateFactory(){};
}
