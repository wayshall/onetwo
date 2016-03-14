package org.onetwo.common.spring.underline;

import org.onetwo.common.utils.StringUtils;

public class SimplePropertyFilters {
	/*public static interface IgnoreNoWritablePropertyFilter extends PropertyFilter {

		default public boolean isCopiable(PropertyDescriptor toProperty, Object fromPropertyValue){
			return IGNORE_NO_WRITABLE.isCopiable(toProperty, fromPropertyValue) & canCopiable(toProperty, fromPropertyValue);
		}
		boolean canCopiable(PropertyDescriptor toProperty, Object fromPropertyValue);
	}*/
	/*public static final PropertyFilter IGNORE_NO_WRITABLE =(toProperty, fromValue) -> {
		return toProperty.getWriteMethod()!=null;
	};*/
	public static final PropertyFilter IGNORE_NULL = (toProperty, fromValue) -> {
		return fromValue!=null;
	};
	
	public static final PropertyFilter IGNORE_BLANK_STRING = (toProperty, fromValue)->{
		return toProperty.getWriteMethod()!=null && 
				(!String.class.isInstance(fromValue) || StringUtils.isNotBlank(fromValue.toString()));
	};

	/*@Override
	public boolean isCopiable(PropertyDescriptor toProperty, Object fromValue) {
		return toProperty.getWriteMethod()!=null && fromValue!=null;
	}*/

}
