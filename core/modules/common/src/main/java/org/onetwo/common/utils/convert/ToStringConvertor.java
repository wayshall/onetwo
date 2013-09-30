package org.onetwo.common.utils.convert;

import org.onetwo.common.utils.LangUtils;

public class ToStringConvertor implements TypeConvert<String> {

	public ToStringConvertor() {
	}
	
	@Override
	public String convert(Object value, Class<?> componentType) {
		if (value == null)
            return LangUtils.EMPTY_STRING;
        return value.toString();

	}

}
