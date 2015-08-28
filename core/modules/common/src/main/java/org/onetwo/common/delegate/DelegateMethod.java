package org.onetwo.common.delegate;

public interface DelegateMethod {

	public Object invoke(Object... args);

	public Object getTarget();

}