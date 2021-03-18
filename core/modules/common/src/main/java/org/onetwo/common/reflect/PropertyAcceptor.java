package org.onetwo.common.reflect;

/**
 * @author weishao zeng
 * <br/>
 */
public interface PropertyAcceptor {

	boolean apply(PropertyContext prop, Object val);
	
}

