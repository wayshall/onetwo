package org.onetwo.common.utils.convert;

public interface Convertor {

	public Convertor register(TypeConvert<?> convertor,
			Class<?>... classes);

	public <T> T convert(Object value, Class<T> targetClass);

	public <T> T convert(Object value, Class<?> targetClass, Class<?> componentType);

}