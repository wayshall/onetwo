package org.onetwo.common.utils.convert;

public class ToLongConvertor implements TypeConvert <Long> {

	@Override
	public Long convert(Object value, Class<?> componentType) {
		if (value == null)
            return 0L;
        Class<?> c = value.getClass();
        if (c.getSuperclass() == Number.class)
            return ((Number) value).longValue();
        if (c == Boolean.class)
            return ((Boolean) value).booleanValue() ? 1L : 0L;
        if (c == Character.class)
            return (long)((Character) value).charValue();
        return Long.parseLong(value.toString().trim());

	}

}
