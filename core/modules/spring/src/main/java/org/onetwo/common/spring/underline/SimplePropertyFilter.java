package org.onetwo.common.spring.underline;

import java.beans.PropertyDescriptor;

public class SimplePropertyFilter implements PropertyFilter {

	@Override
	public boolean isCopiable(PropertyDescriptor toProperty) {
		return toProperty.getWriteMethod()!=null;
	}

	@Override
	public boolean isCopiable(Object fromValue) {
		return fromValue!=null;
	}
	
	

}
