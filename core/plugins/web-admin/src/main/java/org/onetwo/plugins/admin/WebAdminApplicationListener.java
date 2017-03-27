package org.onetwo.plugins.admin;

import org.onetwo.common.spring.SpringUtils;
import org.onetwo.common.spring.Springs.SpringsInitEvent;
import org.onetwo.ext.security.utils.SecurityConfig;
import org.springframework.context.ApplicationListener;

public class WebAdminApplicationListener implements ApplicationListener<SpringsInitEvent> {

	@Override
	public void onApplicationEvent(SpringsInitEvent event) {
		SecurityConfig securityConfig = SpringUtils.getBean(event.getApplicationContext(), SecurityConfig.class);
		if(securityConfig!=null){
			String targetUrl = "/web-admin/index";
//			logger.info("targetUrl: "+targetUrl);
			securityConfig.setAfterLoginUrl(targetUrl);
		}
	}
	
	

}
