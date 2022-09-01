package org.onetwo.common.convert;

import java.util.List;

/**
 * @author weishao zeng
 * <br/>
 */
public interface TypeConvertable {
	
	Object getConvertableValue();

	/****
	 * 解释source为clazz类型的值，若对应的解释器错误，则返回默认值defValue
	 * @author weishao zeng
	 * @param source
	 * @param clazz
	 * @param defValue
	 * @return
	 */
	default <T> T asValue(Class<T> clazz, T defValue) {
		return Types.asValue(getConvertableValue(), clazz, defValue);
	}
	

	/***
	 * source为null，则返回默认值0
	 * @author weishao zeng
	 * @param source
	 * @return
	 */
	default Long asLong() {
		return Types.asLong(getConvertableValue());
	}

	/****
	 * 解释source为integer类型的值，若source无法解释，则抛错
	 * @author weishao zeng
	 * @param source
	 * @return
	 */
	default Integer asInteger() {
		return Types.asInteger(getConvertableValue());
	}
	
	default Boolean asBoolean() {
		return Types.asBoolean(getConvertableValue());
	}

	default String asString() {
		return Types.asString(getConvertableValue());
	}
	
	default <T> T asValue(Class<T> clazz) {
		return Types.asValue(getConvertableValue(), clazz);
	}


	default <T> T[] asArray(Class<T> clazz) {
		return Types.asArray(getConvertableValue(), clazz);
	}

	default <T> T[] asArray(Class<T> clazz, T[] defValue) {
		return Types.asArray(getConvertableValue(), clazz, defValue);
	}

	default <T> List<T> asList(Class<T> clazz) {
		return Types.asList(getConvertableValue(), clazz);
	}

	default <T> List<T> asList(Class<T> clazz, List<T> defValue) {
		return Types.asList(getConvertableValue(), clazz, defValue);
	}
	
}
