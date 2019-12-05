package org.onetwo.common.convert;

import java.util.List;

import org.onetwo.common.log.JFishLoggerFactory;
import org.slf4j.Logger;

final public class Types {

	private static final Convertor instance = new DefaultTypeConvertors();
	private static final Logger logger = JFishLoggerFactory.getLogger(Types.class);

	public static <T> T convertValue(Object source, Class<T> clazz) {
//		return convertValue(source, clazz, null);
		return convertor().convert(source, clazz);
	}

	public static <T> T convertValue(Object source, Class<T> clazz, T defValue) {
		try {
			if(source==null)
				return defValue;
			T val = convertor().convert(source, clazz);
			return val==null?defValue:val;
		} catch (Exception e) {
			logger.error("convert source[{}] to class[{}] error: " + e.getMessage(), source, clazz);
			return defValue;
		}
	}

	/***
	 * source为null，则返回默认值0
	 * @author weishao zeng
	 * @param source
	 * @return
	 */
	public static Long asLong(Object source) {
		return convertor().convert(source, Long.class);
	}

	public static Integer asInteger(Object source) {
		return convertor().convert(source, Integer.class);
	}

	public static String asString(Object source) {
		return convertor().convert(source, String.class);
	}
	
	public static <T> T asValue(Object source, Class<T> clazz) {
//		return asValue(source, clazz, null);
		return convertor().convert(source, clazz);
	}

	public static <T> T asValue(Object source, Class<T> clazz, T defValue) {
		return convertValue(source, clazz, defValue);
	}

	public static <T> T[] asArray(Object source, Class<T> clazz) {
//		return asArray(source, clazz, null);
		return convertor().convert(source, Object[].class, clazz);
	}

	public static <T> T[] asArray(Object source, Class<T> clazz, T[] defValue) {
		try {
			if(source==null)
				return defValue;
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
			if(source==null)
				return defValue;
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
	
	private Types(){
	}
}
