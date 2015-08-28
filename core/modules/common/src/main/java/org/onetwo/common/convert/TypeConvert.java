package org.onetwo.common.convert;

public interface TypeConvert<T> {

	public T convertNotNull(Object source, Class<?> componentType);
	public T convert(Object source, Class<?> componentType);
	
}
