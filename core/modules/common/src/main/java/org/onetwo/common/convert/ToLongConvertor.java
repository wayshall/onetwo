package org.onetwo.common.convert;

import org.onetwo.common.utils.StringUtils;

public class ToLongConvertor extends AbstractTypeConvert<Long> {
	
	public ToLongConvertor() {
		super(0L);
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
        } else {
        	String str = value.toString().trim();
        	if (StringUtils.isBlank(str)) {
        		return null;
        	}
        	return Long.parseLong(str);
        }

	}

}
