package org.onetwo.common.spring.copier;

import java.beans.PropertyDescriptor;

import org.springframework.beans.BeanWrapper;


public interface PropertyValueCopier {

	void copyPropertyValue(SimpleBeanCopier beanCopier, BeanWrapper targetBeanWrapper, PropertyDescriptor targetProperty, Object value);
}
