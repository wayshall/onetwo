package org.onetwo.common.utils;
/**
 * @author weishao zeng
 * <br/>
 */
public interface PageableRequest {
	
	<E> Page<E> toPageObject();

}
