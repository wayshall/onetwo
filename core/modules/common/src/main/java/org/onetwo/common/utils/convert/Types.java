package org.onetwo.common.utils.convert;

import java.util.List;

import org.onetwo.common.log.MyLoggerFactory;
import org.slf4j.Logger;

public class Types {

	private static final Convertor instance = new DefaultTypeConvertors();
	private static final Logger logger = MyLoggerFactory.getLogger(Types.class);

	public static <T> T convertValue(Object source, Class<T> clazz) {
//		return convertValue(source, clazz, null);
		return convertor().convert(source, clazz);
	}

	public static <T> T convertValue(Object source, Class<T> clazz, T defValue) {
		try {
			T val = convertor().convert(source, clazz);
			return val==null?defValue:val;
		} catch (Exception e) {
			logger.error("convert source[{}] to class[{}] error: " + e.getMessage(), source, clazz);
			return defValue;
		}
	}
	public static <T> T asValue(Object source, Class<T> clazz) {
//		return asValue(source, clazz, null);
		return convertor().convert(source, clazz);
	}

	public static <T> T asValue(Object source, Class<T> clazz, T defValue) {
		try {
			T val = convertor().convert(source, clazz);
			return val==null?defValue:val;
		} catch (Exception e) {
			logger.error("convert source[{}] to class[{}] error: " + e.getMessage(), source, clazz);
			return defValue;
		}
	}

	public static <T> T[] asArray(Object source, Class<T> clazz) {
//		return asArray(source, clazz, null);
		return convertor().convert(source, Object[].class, clazz);
	}

	public static <T> T[] asArray(Object source, Class<T> clazz, T[] defValue) {
		try {
			T[] val = convertor().convert(source, Object[].class, clazz);
			return val==null?defValue:val;
		} catch (Exception e) {
			logger.error("convert source[{}] to class[{}] error: " + e.getMessage(), source, clazz);
			return defValue;
		}
	}

	public static <T> List<T> asList(Object source, Class<T> clazz) {
//		return asList(source, clazz, null);
		return convertor().convert(source, List.class, clazz);
	}

	public static <T> List<T> asList(Object source, Class<T> clazz, List<T> defValue) {
		try {
			List<T> val = convertor().convert(source, List.class, clazz);
			return val==null?defValue:val;
		} catch (Exception e) {
			logger.error("convert source[{}] to class[{}] error: " + e.getMessage(), source, clazz);
			return defValue;
		}
	}
	
	public final Convertor register(TypeConvert<?> convertor, Class<?> classes){
		return convertor().register(convertor, classes);
	}
	
	public static Convertor convertor() {
		return instance;
	}
}
