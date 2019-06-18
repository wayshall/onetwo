package org.onetwo.common.spring.validator;

import java.util.List;

/**
 * @author weishao zeng
 * <br/>
 */
public interface ContentChecker {
	
	public List<String> check(String content);

}
