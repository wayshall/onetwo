package org.onetwo.common.reflect;


public interface PropertyCopyer<T> {
	
	/****
	 * 
	 * @param source
	 * @param target
	 * @param property of target
	 */
	public void copy(Object source, Object target, T property);

}
