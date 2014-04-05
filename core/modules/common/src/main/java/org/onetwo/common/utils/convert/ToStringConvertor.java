package org.onetwo.common.utils.convert;


public class ToStringConvertor extends AbstractTypeConvert<String>{

	public ToStringConvertor() {
	}
	
	@Override
	public String doConvert(Object value, Class<?> componentType) {
//		if (value == null)
//			return defValue;
//            return LangUtils.EMPTY_STRING;
        return value.toString();

	}

}
