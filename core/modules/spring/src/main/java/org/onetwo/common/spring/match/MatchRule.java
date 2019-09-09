package org.onetwo.common.spring.match;
/**
 * @author weishao zeng
 * <br/>
 */
public interface MatchRule {
	
	boolean match(String text, String matchValue);

}
