package org.onetwo.common.reflect;

import org.onetwo.common.reflect.BeanToMapConvertor.PropertyContext;

/**
 * @author weishao zeng
 * <br/>
 */
public interface PropertyAcceptor {

	boolean apply(PropertyContext prop, Object val);
	
}

