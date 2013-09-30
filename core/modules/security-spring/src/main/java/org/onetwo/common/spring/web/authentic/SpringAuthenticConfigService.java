package org.onetwo.common.spring.web.authentic;

import org.apache.commons.lang.StringUtils;
import org.onetwo.common.exception.ServiceException;
import org.onetwo.common.spring.SpringUtils;
import org.onetwo.common.web.s2.security.AuthenticationInvocation;
import org.onetwo.common.web.s2.security.SecurityTarget;
import org.onetwo.common.web.s2.security.config.AbstractConfigBuilder;
import org.onetwo.common.web.s2.security.config.AuthenticConfig;
import org.onetwo.common.web.s2.security.service.AbstractAuthenticConfigService;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

//@Service(AuthenticConfigService.NAME)
public class SpringAuthenticConfigService extends AbstractAuthenticConfigService implements ApplicationContextAware {

	private ApplicationContext applicationContext;
	private AbstractConfigBuilder configBuilder;
	

	public SpringAuthenticConfigService() {
	}
	
	public SpringAuthenticConfigService(ApplicationContext applicationContext) {
		super();
		this.applicationContext = applicationContext;
	}

	public AuthenticationInvocation getAuthenticationInvocation(AuthenticConfig config){
		String authName = config.getAuthenticationName();
		if(StringUtils.isBlank(authName))
			authName = AuthenticationInvocation.NAME;
		if(applicationContext.containsBean(authName)){
			return applicationContext.getBean(authName, AuthenticationInvocation.class);
		}else{
			throw new ServiceException("can not find the bean of AuthenticationInvocation: " + authName);
		}
	}
	
	@Override
	public AuthenticConfig getConfig(SecurityTarget target) {
		SpringSecurityTarget starget = (SpringSecurityTarget) target;
		Class<?> clazz = starget.getInvocation().getBeanType();
		AuthenticConfig config = this.findAuthenticConfig(clazz, starget.getInvocation().getMethod());
		return config;
	}

	@Override
	protected AbstractConfigBuilder getConfigBuilder() {
		return configBuilder;
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
		AbstractConfigBuilder configBuilder = SpringUtils.getBean(applicationContext, AbstractConfigBuilder.class);
		if(configBuilder==null){
			logger.info("no security configBuilder, user default : " + SpringConfigBuilder.class);
			configBuilder = new SpringConfigBuilder();
		}else{
			logger.info("user custom security configBuilder : " + configBuilder.getClass());
		}
		this.configBuilder = configBuilder;
	}

}
