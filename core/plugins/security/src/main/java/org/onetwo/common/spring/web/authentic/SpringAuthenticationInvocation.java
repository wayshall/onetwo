package org.onetwo.common.spring.web.authentic;

import org.onetwo.common.spring.SpringUtils;
import org.onetwo.common.sso.SecurityService;
import org.onetwo.common.web.login.EmptyUserLoginService;
import org.onetwo.common.web.s2.security.AbstractAuthenticationInvocation;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;

//@Service(AuthenticationInvocation.NAME)
public class SpringAuthenticationInvocation extends AbstractAuthenticationInvocation implements InitializingBean, ApplicationContextAware {

	private ExpressionParser parser = new SpelExpressionParser();
	
	private ApplicationContext applicationContext;
	private SecurityService ssoService;

	
	@Override
	public void afterPropertiesSet() throws Exception {
		SecurityService ssoService = SpringUtils.getHighestOrder(applicationContext, SecurityService.class);
		if(ssoService==null){
//			ssoService = new EmptyUserLoginService();
//			SpringUtils.registerSingleton(applicationContext, "emptyUserLoginService", ssoService);
			ssoService = SpringUtils.registerBean(applicationContext, EmptyUserLoginService.class);
		}
		this.ssoService = ssoService;
//		Assert.notNull(ssoService, "you must implements a SecurityService, or extends AbstractUserLoginServiceImpl if the app is not a sso login.");
	}

	@Override
	public SecurityService getSecurityService() {
		return ssoService;
	}

	@Override
	protected Object getContextValue(String expr) {
		return parser.parseExpression(expr).getValue();
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}

}
