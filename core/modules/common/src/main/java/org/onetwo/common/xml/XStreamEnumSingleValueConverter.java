package org.onetwo.common.xml;

import java.util.stream.Stream;

import org.onetwo.common.exception.BaseException;

import com.thoughtworks.xstream.converters.basic.AbstractSingleValueConverter;

/**
 * @author weishao zeng <br/>
 */
@SuppressWarnings("rawtypes")
public class XStreamEnumSingleValueConverter extends AbstractSingleValueConverter {
	
	private final Class<? extends Enum> enumType;

	public XStreamEnumSingleValueConverter(Class<? extends Enum> type) {
		if (!Enum.class.isAssignableFrom(type) && type != Enum.class) {
			throw new IllegalArgumentException("Converter can only handle defined enums");
		}
		enumType = type;
	}

	@Override
	public boolean canConvert(Class type) {
		return enumType.isAssignableFrom(type) && XStreamEnumValueMapper.class.isAssignableFrom(type);
	}

	@Override
	public String toString(Object obj) {
		return XStreamEnumValueMapper.class.cast(obj).getEnumValue();
	}

	@Override
	public Object fromString(String str) {
		XStreamEnumValueMapper[] enums = (XStreamEnumValueMapper[])enumType.getEnumConstants();
		XStreamEnumValueMapper valueMapping = Stream.of(enums)
				.filter(dvm->dvm.getEnumValue().equals(str))
				.findFirst()
				.orElseThrow(()-> {
					return new BaseException("error enum mapping value: " + str);
				});
		return valueMapping;
	}
}
