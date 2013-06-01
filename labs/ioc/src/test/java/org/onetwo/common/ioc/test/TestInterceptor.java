package org.onetwo.common.ioc.test;

import org.onetwo.common.ioc.proxy.BFInterceptor;
import org.onetwo.common.ioc.proxy.InvocationContext;

public class TestInterceptor implements BFInterceptor {
	
	@Override
	public Object intercept(InvocationContext invocation) {
		System.out.println(this+":"+invocation.getTarget().getClass().getSimpleName()+" say :==========>>>>>>>>>>>>>>>>>>>>> just test~~~");
		Object result =  invocation.proceed();
		return TestInterceptor.class.getSimpleName();
	}

}
