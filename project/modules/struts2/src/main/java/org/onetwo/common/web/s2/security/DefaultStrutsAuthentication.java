package org.onetwo.common.web.s2.security;


import org.onetwo.common.spring.SpringApplication;
import org.onetwo.common.sso.SSOService;
import org.springframework.stereotype.Service;

/********
 * 验证器，
 * 子类覆盖getSSOService方法，提供一个最简单的实现
 * 默认将会查找 AuthenticationInvocation.NAME 名字的实现
 */
@Service(AuthenticationInvocation.NAME)
public class DefaultStrutsAuthentication extends StrutsAuthentication{

	private SSOService SSOService;
	

	@Override
	public SSOService getSSOService() {
		if(SSOService==null)
			SSOService = SpringApplication.getInstance().getFirstImplementor(SSOService.class, true);
		return SSOService;
	}
	
}
