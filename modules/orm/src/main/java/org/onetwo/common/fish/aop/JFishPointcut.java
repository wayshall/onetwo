package org.onetwo.common.fish.aop;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

@Aspect
public class JFishPointcut {

	@Pointcut("execution (* *query*(..)) || execution (* *execute*(..)) || execution (* *batchUpdate*(..)) || execution (* *update*(..))")
	public void jdbcTemplate(){}
	
}
