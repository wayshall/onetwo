package org.onetwo.common.spring.underline;

import org.springframework.beans.BeanWrapper;


public interface PropertyValueSetter {

	void setPropertyValue(BeanWrapper targetBeanWrapper, String propertyName, Object value);
}
