package org.onetwo.common.spring.underline;

import java.beans.PropertyDescriptor;

public interface PropertyFilter {

	boolean isCopiable(PropertyDescriptor toProperty);
	boolean isCopiable(Object fromValue);

}
