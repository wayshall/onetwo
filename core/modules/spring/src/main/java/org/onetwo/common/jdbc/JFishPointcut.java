package org.onetwo.common.jdbc;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

@Aspect
public class JFishPointcut {

	@Pointcut("execution (* *query*(..)) || execution (* *execute*(..)) || execution (* *batchUpdate*(..)) || execution (* *update*(..))")
	public void jdbcTemplate(){}

	/*@Pointcut("execution (* *save*(..))")
	public void profilerMethod(){}*/
	
}
