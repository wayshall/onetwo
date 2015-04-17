package org.onetwo.plugins.security;

import java.util.List;

import org.onetwo.common.spring.plugin.AbstractContextPlugin;
import org.onetwo.plugins.security.client.SsoClientContext;
import org.onetwo.plugins.security.common.SecurityContext;
import org.onetwo.plugins.security.common.SsoConfig;
import org.onetwo.plugins.security.server.SsoServerContext;
import org.onetwo.plugins.security.utils.SecurityPluginUtils;


public class SecurityPlugin extends AbstractContextPlugin<SecurityPlugin> {

	private static SecurityPlugin instance;
	
	
	public static SecurityPlugin getInstance() {
		return instance;
	}
	
	@Override
	public void setPluginInstance(SecurityPlugin plugin){
		instance = plugin;
	}
	
	private SsoConfig ssoConfig;
//	private SecurityMode loginMode;
	

	public void setSsoConfig(SsoConfig ssoConfig) {
		this.ssoConfig = ssoConfig;
	}

	public SsoConfig getSsoConfig() {
		return ssoConfig;
	}
	
	public boolean isSsoEnable(){
		return ssoConfig!=null;
	}

	@Override
	public void onJFishContextClasses(List<Class<?>> annoClasses) {
		annoClasses.add(SecurityContext.class);
		
		if(SecurityPluginUtils.existServerConfig()){
			annoClasses.add(SsoServerContext.class);
			
		}else if(SecurityPluginUtils.existClientConfig()){
			annoClasses.add(SsoClientContext.class);
		}else{
//			annoClasses.add(NotSsoContext.class);
		}
	}

	/*@Configuration
	public static class NotSsoContext {

		@Bean
		public SSOService ssoService(){
			SimpleNotSSOServiceImpl ssoservice = new SimpleNotSSOServiceImpl();
			return ssoservice;
		}
	}*/

	/*public static enum SecurityMode {
		COMMON,
		SSO_CLIENT,
		SSO_SERVER
	}*/
}
