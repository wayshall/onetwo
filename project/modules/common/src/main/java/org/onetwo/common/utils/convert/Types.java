package org.onetwo.common.utils.convert;

import java.util.List;

public class Types {

	private static final Convertor instance = new DefaultTypeConvertors();

	public static <T> T convertValue(Object source, Class<T> clazz) {
		return convertor().convert(source, clazz);
	}
	public static <T> T asValue(Object source, Class<T> clazz) {
		return convertor().convert(source, clazz);
	}

	public static <T> T[] asArray(Object source, Class<T> clazz) {
		return convertor().convert(source, Object[].class, clazz);
	}

	public static <T> List<T> asList(Object source, Class<T> clazz) {
		return convertor().convert(source, List.class, clazz);
	}
	
	public final Convertor register(TypeConvert<?> convertor, Class<?>... classes){
		return convertor().register(convertor, classes);
	}
	
	public static Convertor convertor() {
		return instance;
	}
}
