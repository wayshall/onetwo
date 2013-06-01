package org.onetwo.common.ioc.test;

import org.onetwo.common.ioc.annotation.Inject;
import org.onetwo.common.ioc.proxy.BFInterceptor;
import org.onetwo.common.ioc.proxy.InvocationContext;

public class TestOrderInterceptor implements BFInterceptor {
	
	@Inject
	protected AuthTestService authTestService;

	@Override
	public Object intercept(InvocationContext invocation) {
		System.out.println(this+":"+invocation.getTarget().getClass().getSimpleName()+" say :==========>>>>>>>>>>>>>>>>>>>>> just test~~~");
		Object result =  invocation.proceed();
		authTestService.printAuths();
		return TestOrderInterceptor.class.getSimpleName();
	}

}
