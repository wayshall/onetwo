package org.onetwo.common.spring.underline;

import java.beans.PropertyDescriptor;

public class SimplePropertyFilter implements PropertyFilter {

	@Override
	public boolean isCopiable(PropertyDescriptor toProperty, Object fromValue) {
		return toProperty.getWriteMethod()!=null && fromValue!=null;
	}

}
