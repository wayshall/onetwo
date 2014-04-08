package org.onetwo.common.utils.convert;

import java.util.List;

public class Types {

	private static final Convertor instance = new DefaultTypeConvertors();

	public static <T> T convertValue(Object source, Class<T> clazz) {
		return convertValue(source, clazz, null);
	}

	public static <T> T convertValue(Object source, Class<T> clazz, T defValue) {
		T val = convertor().convert(source, clazz);
		return val==null?defValue:val;
	}
	public static <T> T asValue(Object source, Class<T> clazz) {
		return asValue(source, clazz, null);
	}

	public static <T> T asValue(Object source, Class<T> clazz, T defValue) {
		T val = convertor().convert(source, clazz);
		return val==null?defValue:val;
	}

	public static <T> T[] asArray(Object source, Class<T> clazz) {
		return asArray(source, clazz, null);
	}

	public static <T> T[] asArray(Object source, Class<T> clazz, T[] defValue) {
		T[] val = convertor().convert(source, Object[].class, clazz);
		return val==null?defValue:val;
	}

	public static <T> List<T> asList(Object source, Class<T> clazz) {
		return asList(source, clazz, null);
	}

	public static <T> List<T> asList(Object source, Class<T> clazz, List<T> defValue) {
		List<T> val = convertor().convert(source, List.class, clazz);
		return val==null?defValue:val;
	}
	
	public final Convertor register(TypeConvert<?> convertor, Class<?> classes){
		return convertor().register(convertor, classes);
	}
	
	public static Convertor convertor() {
		return instance;
	}
}
