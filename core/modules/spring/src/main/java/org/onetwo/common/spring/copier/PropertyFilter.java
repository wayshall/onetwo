package org.onetwo.common.spring.copier;

import java.beans.PropertyDescriptor;

public interface PropertyFilter {

	/***
	 * true 表示可复制，false不能
	 * @param toProperty
	 * @param fromPropertyValue
	 * @return
	 */
	boolean isCopiable(PropertyDescriptor toProperty, Object fromPropertyValue);

}
