package org.onetwo.common.utils;


public interface PropertyCopyer<T> {
	
	/****
	 * 
	 * @param source
	 * @param target
	 * @param property of target
	 */
	public void copy(Object source, Object target, T property);

}
