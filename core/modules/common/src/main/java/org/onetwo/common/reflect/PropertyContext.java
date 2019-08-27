package org.onetwo.common.reflect;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;

/**
 * @author weishao zeng
 * <br/>
 */
public interface PropertyContext {

	Object getSource();

	PropertyDescriptor getProperty();

	Field getField();

	String getName();

	String getPrefix();

	PropertyContext getParent();

}
