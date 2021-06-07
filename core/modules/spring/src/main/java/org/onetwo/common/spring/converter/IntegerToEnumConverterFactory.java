package org.onetwo.common.spring.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.converter.ConverterFactory;

/**
 * @author weishao zeng
 * <br/>
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class IntegerToEnumConverterFactory implements ConverterFactory<Integer, Enum> {

	@Override
	public <R extends Enum> Converter<Integer, R> getConverter(Class<R> targetType) {
		return new IntegerToEnum(targetType);
	}

	private class IntegerToEnum<T extends Enum> implements Converter<Integer, T> {

		private final Class<T> enumType;
		
		public IntegerToEnum(Class<T> enumType) {
			super();
			this.enumType = enumType;
		}

		@Override
		public T convert(Integer source) {
			T[] values = (T[]) enumType.getEnumConstants();
			int ordinal = source;
			return ordinal>values.length?null:values[ordinal];
		}
		
		
	}

}
