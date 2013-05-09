package org.onetwo.common.utils.convert;

public interface TypeConvert<T> {

	public T convert(Object source, Class<?> componentType);
	
}
