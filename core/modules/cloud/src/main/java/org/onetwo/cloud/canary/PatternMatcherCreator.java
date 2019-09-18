package org.onetwo.cloud.canary;

import org.onetwo.boot.limiter.Matcher;

/**
 * @author weishao zeng
 * <br/>
 */
public interface PatternMatcherCreator {

	/***
	 * @author weishao zeng
	 * @param patterns
	 * @return
	 */
	Matcher<CanaryContext> apply(String[] patterns);
	
}

