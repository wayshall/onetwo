package org.onetwo.common.ioc.test;

import org.onetwo.common.ioc.annotation.BFComponent;
import org.onetwo.common.ioc.annotation.Inject;

@BFComponent(businessInterfaces=RoleTestSerivce.class, interceptors=TestInterceptor.class)
public class RoleTestSerivceImpl implements RoleTestSerivce {

	private AuthTestService authTestService;
	
	public String getClassSimpleName(){
		System.out.println("AuthTestService:"+this+": printRole");
		this.authTestService.printAuths();
		
		return RoleTestSerivceImpl.class.getSimpleName();
	}

	@Inject
	public void setAuthTestService(AuthTestService authTestService) {
		this.authTestService = authTestService;
	}
}
