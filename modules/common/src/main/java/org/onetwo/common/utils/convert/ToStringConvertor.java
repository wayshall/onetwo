package org.onetwo.common.utils.convert;

import org.onetwo.common.utils.LangUtils;

public class ToStringConvertor extends AbstractTypeConvert<String> {


	public ToStringConvertor(Convertor convertor) {
		super(convertor);
	}
	
	@Override
	public String convert(Object value, Class<?> componentType) {
		if (value == null)
            return LangUtils.EMPTY_STRING;
        return value.toString();

	}

}
