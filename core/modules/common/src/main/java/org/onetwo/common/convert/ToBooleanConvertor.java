package org.onetwo.common.convert;

import java.util.Arrays;
import java.util.List;

import org.onetwo.common.utils.StringUtils;

public class ToBooleanConvertor extends AbstractTypeConvert<Boolean> {

	public static final List<String> FALSE_VALUES = Arrays.asList("false", "no", "否");
	public static final List<String> TRUE_VALUES = Arrays.asList("true", "yes", "是");

	
	public ToBooleanConvertor() {
		super(false);
	}


	@Override
	public Boolean doConvert(Object value, Class<?> componentType) {
		if (value == null)
			return false;
		Class<?> c = value.getClass();
		if (c.getSuperclass() == Number.class)
			return ((Number) value).intValue() != 0;
		if (c == Boolean.class)
			return (Boolean) value;
		if (c == Character.class)
			return ((Character) value).charValue() != 0;
		if (CharSequence.class.isAssignableFrom(c)) {
			if (StringUtils.isBlank(value.toString()) || FALSE_VALUES.contains(value.toString().toLowerCase())) {
				return false;
			}else if(TRUE_VALUES.contains(value.toString().toLowerCase())){
				return true;
			}else{
				throw new IllegalArgumentException("can not convert string["+value+"] to bolean!");
			}
		}

		throw new IllegalArgumentException("can not convert object["+value+"] to bolean!");

	}

}
