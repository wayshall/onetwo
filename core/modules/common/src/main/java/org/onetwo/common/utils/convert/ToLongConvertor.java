package org.onetwo.common.utils.convert;

public class ToLongConvertor extends AbstractTypeConvert<Long> {
	
	public ToLongConvertor(Long defValue) {
		super(defValue);
	}

	@Override
	public Long doConvert(Object value, Class<?> componentType) {
        Class<?> c = value.getClass();
        if (c.getSuperclass() == Number.class)
            return ((Number) value).longValue();
        if (c == Boolean.class)
            return ((Boolean) value).booleanValue() ? 1L : 0L;
        if (c == Character.class){
            return (long)((Character) value).charValue();
        }
        return Long.parseLong(value.toString().trim());

	}

}
