package org.onetwo.common.utils.convert;

import org.onetwo.common.utils.LangUtils;


public class ToStringConvertor extends AbstractTypeConvert<String>{

	public ToStringConvertor() {
		super(LangUtils.EMPTY_STRING);
	}
	
	@Override
	public String doConvert(Object value, Class<?> componentType) {
//		if (value == null)
//			return defValue;
//            return LangUtils.EMPTY_STRING;
        return value.toString();

	}

}
