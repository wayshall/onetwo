package org.onetwo.common.ioc.test;

import org.onetwo.common.ioc.annotation.BFComponent;
import org.onetwo.common.ioc.annotation.BFInterceptors;
import org.onetwo.common.ioc.annotation.Inject;

@BFInterceptors({TestOrderInterceptor.class})
@BFComponent(name="userTest", businessInterfaces=UserTestService.class, interceptors=TestInterceptor.class)
public class UserTestServiceImpl implements UserTestService {
	
	@Inject
	private RoleTestSerivce roleTestSerivce;
	
	public String outString(){
		System.out.println("UserTestService: " +this + ", role: " + this.roleTestSerivce);
		this.roleTestSerivce.getClassSimpleName();
		return UserTestServiceImpl.class.getSimpleName();
	}

}
