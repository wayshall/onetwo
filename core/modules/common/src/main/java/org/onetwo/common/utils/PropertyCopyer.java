package org.onetwo.common.utils;


public interface PropertyCopyer<T> {
	
	public void copy(Object source, Object target, T property);

}
