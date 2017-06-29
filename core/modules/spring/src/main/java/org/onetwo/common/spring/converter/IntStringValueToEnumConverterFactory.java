package org.onetwo.common.spring.converter;

import org.onetwo.common.convert.Types;
import org.onetwo.common.reflect.TypeResolver;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.converter.ConverterFactory;
import org.springframework.util.Assert;

@SuppressWarnings({ "unchecked", "rawtypes" })
public class IntStringValueToEnumConverterFactory implements ConverterFactory<String, Enum> {

	public <T extends Enum> Converter<String, T> getConverter(Class<T> targetType) {
		Class<?> enumType = targetType;
		while(enumType != null && !enumType.isEnum()) {
			enumType = enumType.getSuperclass();
		}
		Assert.notNull(enumType, "The target type " + targetType.getName()
				+ " does not refer to an enum");
		return new StringToEnum(enumType);
	}

	private class StringToEnum<T extends Enum> implements Converter<String, T> {

		private final Class<T> enumType;

		public StringToEnum(Class<T> enumType) {
			this.enumType = enumType;
		}

		public T convert(String source) {
			if(ValueEnum.class.isAssignableFrom(enumType)){
				Class<T> genericClass = (Class<T>)TypeResolver.resolveRawArgument(ValueEnum.class, enumType);
				T value = Types.convertValue(source, genericClass);
				return Types.asValue(value, enumType);
				/*Method staticMehtod = ReflectUtils.findMethod(enumType, "valueOf", int.class);
				Object val;
				try {
					val = staticMehtod.invoke(enumType, value);
				} catch (Exception e) {
					throw new BaseException("convert to int value enum error, does enum type["+enumType+"] has a valueOf(int) method? ", e);
				} 
				return (T) val;*/
			}else{
				if (source.length() == 0) {
					return null;
				}
				try {
					return (T) Enum.valueOf(this.enumType, source.trim());
				} catch (IllegalArgumentException e) {
					try {
						return (T) Enum.valueOf(this.enumType, source.trim().toUpperCase());
					} catch (IllegalArgumentException e2) {
						throw e;
					}
				}
			}
		}
	}

}
