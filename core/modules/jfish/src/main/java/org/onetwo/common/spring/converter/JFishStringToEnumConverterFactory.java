package org.onetwo.common.spring.converter;

import java.lang.reflect.Method;

import org.onetwo.common.exception.BaseException;
import org.onetwo.common.utils.ReflectUtils;
import org.onetwo.common.utils.convert.Types;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.converter.ConverterFactory;
import org.springframework.util.Assert;

@SuppressWarnings({ "unchecked", "rawtypes" })
public class JFishStringToEnumConverterFactory implements ConverterFactory<String, Enum> {

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
			if(IntValueEnum.class.isAssignableFrom(enumType)){
				int value = Types.convertValue(source, Integer.class);
				Method staticMehtod = ReflectUtils.findMethod(enumType, "valueOf", int.class);
				Object val;
				try {
					val = staticMehtod.invoke(enumType, value);
				} catch (Exception e) {
					throw new BaseException("convert to int value enum error, does enum type["+enumType+"] has a valueOf(int) method? ", e);
				} 
				return (T) val;
			}else{
				if (source.length() == 0) {
					// It's an empty enum identifier: reset the enum value to null.
					return null;
				}
				return (T) Enum.valueOf(this.enumType, source.trim());
			}
		}
	}

}
