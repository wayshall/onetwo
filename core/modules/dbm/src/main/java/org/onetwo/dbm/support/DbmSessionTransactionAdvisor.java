package org.onetwo.dbm.support;

import java.lang.reflect.UndeclaredThrowableException;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.onetwo.dbm.annotation.AutoWrapTransactional;
import org.onetwo.dbm.utils.DbmUtils;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.aop.support.annotation.AnnotationMatchingPointcut;

@SuppressWarnings("serial")
public class DbmSessionTransactionAdvisor extends DefaultPointcutAdvisor {
	final static private AnnotationMatchingPointcut AUTO_WRAP_TRANSACTIONAL_METHOD_POINTCUT = AnnotationMatchingPointcut.forMethodAnnotation(AutoWrapTransactional.class);
	
	public DbmSessionTransactionAdvisor(DbmSession session) {
		super(AUTO_WRAP_TRANSACTIONAL_METHOD_POINTCUT, new DbmSessionTransactionAdvice(session));
	}	
	
	static class DbmSessionTransactionAdvice implements MethodInterceptor {
		final private DbmSession session;
		
		public DbmSessionTransactionAdvice(DbmSession session) {
			super();
			this.session = session;
		}

		@Override
		public Object invoke(MethodInvocation invocation) throws Throwable {
			DbmTransaction transaction = session.beginTransaction();
			try {
				Object result = invocation.proceed();
				transaction.commit();
				return result;
			} catch (Exception ex) {
				DbmUtils.rollbackOnException(transaction, ex);
				throw new UndeclaredThrowableException(ex, "TransactionCallback threw undeclared checked exception");
			}
		}
	}
	

}
