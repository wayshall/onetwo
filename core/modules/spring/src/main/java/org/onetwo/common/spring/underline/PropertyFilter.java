package org.onetwo.common.spring.underline;

import java.beans.PropertyDescriptor;

public interface PropertyFilter {

	boolean isCopiable(PropertyDescriptor toProperty, Object fromPropertyValue);

}
