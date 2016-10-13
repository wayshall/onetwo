package org.onetwo.dbm.jdbc;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

@Aspect
public class DbmJdbcTemplateAspectProxy extends BaseJdbcTemplateAspectProxy{
	
	
	@Around("org.onetwo.dbm.jdbc.DbmPointcut.jdbcTemplate()")
	public Object doProfiling(ProceedingJoinPoint pjp) throws Throwable{
//		JdbcContext jdbcContext = JdbcContextHolder.getJdbcContex();
		Context context = new Context(System.currentTimeMillis());
//		context.setJdbcCountInThread(jdbcContext.increaseOperationCount());
		try{
			Object result = pjp.proceed();
			context.setReturnValue(result);
			return result;
		}finally{
			context.setFinishTime(System.currentTimeMillis());
			this.afterProceed(context, pjp);
		}
			
	}
}
