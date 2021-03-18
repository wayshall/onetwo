package org.springframework.cloud.netflix.feign;

import java.util.Arrays;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.onetwo.common.exception.BaseException;
import org.onetwo.common.spring.aop.Proxys;
import org.springframework.aop.support.AopUtils;
import org.springframework.context.ApplicationContext;
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
	private LocalFeignTransactionWrapper transactionWrapper;
	
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
		final Object target;
		if (this.localBean==null) {
			target = applicationContext.getBean(localBeanName);
			this.localBean = target;
		} else {
			target = this.localBean;
		}
		
		if (!TransactionSynchronizationManager.isActualTransactionActive()) {
			return invokeTarget(target, invocation);
		} else {
			return transactionWrapper.wrapRequiresNew(() -> {
				return invokeTarget(target, invocation);
			});
		}
	}
	
	private Object invokeTarget(Object target, MethodInvocation invocation) {
		try {
			return AopUtils.invokeJoinpointUsingReflection(target, invocation.getMethod(), invocation.getArguments());
		} catch (Throwable e) {
			throw new BaseException("invoke local feign client error: " + e.getMessage(), e);
		}
	}

	public void setTransactionWrapper(LocalFeignTransactionWrapper transactionWrapper) {
		this.transactionWrapper = transactionWrapper;
	}

	
}
