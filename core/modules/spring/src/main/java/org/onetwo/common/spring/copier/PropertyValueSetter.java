package org.onetwo.common.spring.copier;

import org.springframework.beans.BeanWrapper;


public interface PropertyValueSetter {

	void setPropertyValue(BeanWrapper targetBeanWrapper, String propertyName, Object value);
}
