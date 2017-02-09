package org.onetwo.common.spring.copier;

import org.onetwo.common.utils.StringUtils;

public class SimplePropertyFilters {
	public static final PropertyFilter IGNORE_NULL = (toProperty, fromValue) -> {
		return fromValue!=null;
	};
	
	public static final PropertyFilter IGNORE_BLANK_STRING = (toProperty, fromValue)->{
		return toProperty.getWriteMethod()!=null && 
				(!String.class.isInstance(fromValue) || StringUtils.isNotBlank(fromValue.toString()));
	};

}
