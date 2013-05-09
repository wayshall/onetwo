package org.onetwo.common.jdbc;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.onetwo.common.fish.utils.JFishHolder;
import org.onetwo.common.fish.utils.JdbcContext;

@Aspect
public class JFishJdbcTemplateProxy extends BaseJdbcTemplateProxy{
	

	@Around("org.onetwo.common.fish.aop.JFishPointcut.jdbcTemplate()")
	public Object doProfiling(ProceedingJoinPoint pjp) throws Throwable{
		JdbcContext jdbcContext = JFishHolder.getJdbcContex();
		Context context = new Context(System.currentTimeMillis());
		context.setJdbcCountInThread(jdbcContext.increaseOperationCount());
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
