package org.onetwo.common.web.s2.security.service;


import java.lang.reflect.Method;

import org.apache.struts2.StrutsConstants;
import org.onetwo.common.utils.AnnotationUtils;
import org.onetwo.common.web.s2.security.SecurityTarget;
import org.onetwo.common.web.s2.security.StrutsSecurityTarget;
import org.onetwo.common.web.s2.security.config.AuthenticConfig;
import org.onetwo.common.web.s2.security.config.AuthenticConfigService;
import org.onetwo.common.web.s2.security.config.ConfigBuilder;
import org.springframework.stereotype.Service;

import com.opensymphony.xwork2.ActionProxy;
import com.opensymphony.xwork2.inject.Inject;

@Service(AuthenticConfigService.NAME)
public class StrutsAuthenticConfigServiceImpl extends AbstractAuthenticConfigService {

	@SuppressWarnings("rawtypes")
	public AuthenticConfig getConfig(SecurityTarget target) {
		StrutsSecurityTarget starget = (StrutsSecurityTarget) target;
		ActionProxy proxy = starget.getInvocation().getProxy();
		Class clazz = starget.getInvocation().getAction().getClass();//ReflectUtils.loadClass(proxy.getConfig().getClassName());
		if(logger.isInfoEnabled())
			logger.info("Authentic class["+proxy.getConfig().getClassName()+"]  method["+proxy.getConfig().getMethodName()+"]");
		return this.findAuthenticConfig(clazz, AnnotationUtils.findMethod(clazz, proxy.getMethod()));
	}
	
	protected ConfigBuilder getConfigBuilder(Class<?> clazz, Method method){
		ConfigBuilder configBuilder = new ConfigBuilder(clazz, method);
		return configBuilder;
	}
	
    @Inject(StrutsConstants.STRUTS_DEVMODE)
    public void setDevMode(String mode) {
        this.devMode = "true".equals(mode);
    }

}
