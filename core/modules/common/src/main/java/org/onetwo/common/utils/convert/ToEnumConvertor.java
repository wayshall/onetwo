package org.onetwo.common.utils.convert;

@SuppressWarnings("rawtypes")
public class ToEnumConvertor implements TypeConvert<Enum<?>> {

	@SuppressWarnings("unchecked")
	@Override
	public Enum<?> convert(Object value, Class<?> componentType) {
		if (value == null)
            return null;
        String name = value.toString();
        return Enum.valueOf((Class)componentType, name);
	}

}
