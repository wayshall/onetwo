package org.onetwo.common.web.s2.security.service;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang.StringUtils;
import org.onetwo.common.log.MyLoggerFactory;
import org.onetwo.common.spring.SpringApplication;
import org.onetwo.common.web.config.BaseSiteConfig;
import org.onetwo.common.web.s2.security.AuthenticationInvocation;
import org.onetwo.common.web.s2.security.config.AbstractConfigBuilder;
import org.onetwo.common.web.s2.security.config.AuthenticConfig;
import org.onetwo.common.web.s2.security.config.AuthenticConfigService;
import org.slf4j.Logger;

abstract public class AbstractAuthenticConfigService implements AuthenticConfigService {

	protected final Logger logger = MyLoggerFactory.getLogger(this.getClass());
	protected Map<String, AuthenticConfig> configs = new ConcurrentHashMap<String, AuthenticConfig>();

	public AbstractAuthenticConfigService(){
	}

	public AuthenticationInvocation getAuthenticationInvocation(AuthenticConfig config){
		String authName = config.getAuthenticationName();
		if(StringUtils.isBlank(authName))
			authName = AuthenticationInvocation.NAME;
		return SpringApplication.getInstance().getBean(AuthenticationInvocation.class, authName);
	}
	
	public AuthenticConfig findAuthenticConfig(Class<?> clazz, Method method) {
		String configName = method.toGenericString();
		AuthenticConfig conf = configs.get(configName);
		if (!BaseSiteConfig.getInstance().isProduct() || conf == null) {
			conf = readConfig(clazz, method);
		}
		return conf;
	}

	protected AuthenticConfig readConfig(Class<?> clazz, Method method) {
		AbstractConfigBuilder configBuilder = getConfigBuilder();
		AuthenticConfig config = configBuilder.buildAuthenConfig(clazz, method);
		configs.put(method.toGenericString(), config.freezing());
		return config;
	}
	
	/******
	 * 可覆盖
	 * @param clazz
	 * @param method
	 * @return
	 */
	abstract protected AbstractConfigBuilder getConfigBuilder();
	
}
