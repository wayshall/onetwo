package org.onetwo.common.web.s2.security.service;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.onetwo.common.spring.SpringApplication;
import org.onetwo.common.utils.StringUtils;
import org.onetwo.common.web.config.BaseSiteConfig;
import org.onetwo.common.web.s2.security.AuthenticationInvocation;
import org.onetwo.common.web.s2.security.config.AbstractConfigBuilder;
import org.onetwo.common.web.s2.security.config.AuthenticConfig;
import org.onetwo.common.web.s2.security.config.AuthenticConfigService;

abstract public class AbstractAuthenticConfigService implements AuthenticConfigService {

	protected final Logger logger = Logger.getLogger(this.getClass());
	protected Map<String, AuthenticConfig> configs = new HashMap<String, AuthenticConfig>();

	protected boolean devMode;

	public AbstractAuthenticConfigService(){
		this.devMode = BaseSiteConfig.getInstance().isDev();
	}

	public AuthenticationInvocation getAuthenticationInvocation(AuthenticConfig config){
		String authName = config.getAuthenticationName();
		if(StringUtils.isBlank(authName))
			authName = AuthenticationInvocation.NAME;
		return SpringApplication.getInstance().getBean(AuthenticationInvocation.class, authName);
	}
	
	public AuthenticConfig findAuthenticConfig(Class<?> clazz, Method method) {
		String configName = method.toGenericString();
		AuthenticConfig conf = null;
		if(!isDevMode())
			conf = configs.get(configName);
		if (conf == null) {
			conf = readConfig(clazz, method);
		}
		return conf;
	}

	protected AuthenticConfig readConfig(Class<?> clazz, Method method) {
		AbstractConfigBuilder configBuilder = getConfigBuilder(clazz, method);
		AuthenticConfig config = configBuilder.buildAuthenConfig();
		configs.put(method.toGenericString(), config);
		return config;
	}
	
	/******
	 * 可覆盖
	 * @param clazz
	 * @param method
	 * @return
	 */
	abstract protected AbstractConfigBuilder getConfigBuilder(Class<?> clazz, Method method);
	
    public void setDevMode(String mode) {
        this.devMode = "true".equals(mode);
    }

	public boolean isDevMode() {
		return devMode;
	}

}
