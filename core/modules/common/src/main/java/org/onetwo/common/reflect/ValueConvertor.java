package org.onetwo.common.reflect;

/**
 * @author weishao zeng
 * <br/>
 */

public interface ValueConvertor {
	Object apply(PropertyContext ctx, Object v);
}
