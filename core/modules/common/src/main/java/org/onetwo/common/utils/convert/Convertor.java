package org.onetwo.common.utils.convert;

public interface Convertor {

	public Convertor register(TypeConvert<?> convertor, Class<?> clazz);

	public <T> T convert(Object value, Class<?> targetClass);
	public <T> T convert(Object value, Class<?> targetClass, Class<?> componentType);
	

	public <T> T convertNotNull(Object source, Class<?> componentType);
	public <T> T convertNotNull(Object value, Class<?> targetClass, Class<?> componentType);

}