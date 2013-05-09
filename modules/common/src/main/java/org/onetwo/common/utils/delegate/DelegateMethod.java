package org.onetwo.common.utils.delegate;

public interface DelegateMethod {

	public Object invoke(Object... args);

	public Object getTarget();

}