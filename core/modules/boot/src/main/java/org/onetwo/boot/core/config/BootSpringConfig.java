package org.onetwo.boot.core.config;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.onetwo.common.propconf.Env;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.ApplicationContext;

import lombok.Data;

/***
 * 需要使用到的spring的配置
 * @author way
 *
 */
@ConfigurationProperties(prefix="spring")
@SuppressWarnings("unchecked")
@Data
public class BootSpringConfig {
	/**
	 * -Dapp.env=product
	 */
	public static final String SYSTEM_APP_ENV_KEY = "app.env";
//	private ProfilesConfig profiles = new ProfilesConfig();
	private ApplicationProperties application = new ApplicationProperties();
	
	@Autowired
	private ApplicationContext applicationContext;
	
    public boolean isEnv(Env env){
    	String envString = env.name().toLowerCase();
    	List<String> activeProfiles = getActiveProfiles();
		boolean res = Optional.ofNullable(activeProfiles)
						.orElse(Collections.EMPTY_LIST)
						.contains(envString);
		return res?true:System.getProperty(SYSTEM_APP_ENV_KEY, "").contains(envString);
	}
	
	public boolean isProduct(){
		return isEnv(Env.PRODUCT);
	}
	
	public boolean isDev(){
		return isEnv(Env.DEV);
	}
	
	public boolean isTest(){
		return isEnv(Env.TEST);
	}
	
	public List<String> getActiveProfiles() {
		return Arrays.asList(applicationContext.getEnvironment().getActiveProfiles());
	}
	
	/*@Data
	public class ProfilesConfig {
		private List<String> active = new ArrayList<String>();
	}
	*/
	@Data
	public class ApplicationProperties {
		String name;
	}
	
}
