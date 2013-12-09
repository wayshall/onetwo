package org.onetwo.common.jdbc;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

@Aspect
public class JFishPointcut {

	@Pointcut("execution (* *query*(..)) || execution (* *execute*(..)) || execution (* *batchUpdate*(..)) || execution (* *update*(..))")
	public void jdbcTemplate(){}

	/*@Pointcut("execution (* *save*(..))")
	public void profilerMethod(){}*/

//	@Pointcut("@within(org.springframework.stereotype.Controller)")
	@Pointcut("@within(org.springframework.stereotype.Controller) && @annotation(org.springframework.web.bind.annotation.RequestMapping)")
//	@Pointcut("execution (* com..*.*Controller.*(..))")
	public void byRequestMapping(){}


	@Pointcut("@annotation(org.springframework.transaction.annotation.Transactional)")
	public void byTransactional(){}
}
