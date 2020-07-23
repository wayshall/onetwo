package org.springframework.cloud.netflix.feign;

import java.util.Arrays;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.onetwo.common.spring.aop.Proxys;
import org.springframework.aop.support.AopUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.transaction.support.TransactionSynchronizationManager;


/**
 * @author weishao zeng
 * <br/>
 */

public class LocalFeignDelegateBean<T> implements MethodInterceptor {
	
	private Class<T> clientInterface;
	final private String localBeanName;
	private Object localBean;
	private ApplicationContext applicationContext;
	private PlatformTransactionManager transactionManager;
	
	public LocalFeignDelegateBean(ApplicationContext applicationContext, Class<T> clientInterface, String localBeanName) {
		super();
		this.clientInterface = clientInterface;
		this.localBeanName = localBeanName;
		this.applicationContext = applicationContext;
	}
	
	public T createProxy() {
		T proxy = Proxys.interceptInterfaces(Arrays.asList(clientInterface), this);
		return clientInterface.cast(proxy);
	}

	/****
	 * 因为远程调用时，必然总是新开事务的，这里使用 REQUIRES_NEW 作为模拟
	 */
//	@Transactional(propagation = Propagation.REQUIRES_NEW)
	@Override
	public Object invoke(MethodInvocation invocation) throws Throwable {
		Object target = this.localBean;
		if(target==null){
			target = applicationContext.getBean(localBeanName);
			this.localBean = target;
		}
		
		if (!TransactionSynchronizationManager.isActualTransactionActive()) {
			return invokeTarget(target, invocation);
		} else {
			TransactionDefinition definition = new DefaultTransactionDefinition(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
			TransactionStatus status = transactionManager.getTransaction(definition);
			try {
				return invokeTarget(target, invocation);
			} catch(Throwable t) {
				transactionManager.rollback(status);
				throw t;
			} finally {
				transactionManager.commit(status);
			}
		}
	}
	
	private Object invokeTarget(Object target, MethodInvocation invocation) throws Throwable {
		return AopUtils.invokeJoinpointUsingReflection(target, invocation.getMethod(), invocation.getArguments());
	}

	public void setTransactionManager(PlatformTransactionManager transactionManager) {
		this.transactionManager = transactionManager;
	}
	
}
