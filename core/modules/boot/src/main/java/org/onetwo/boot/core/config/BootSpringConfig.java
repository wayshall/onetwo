package org.onetwo.boot.core.config;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import lombok.Data;

import org.onetwo.common.propconf.Env;
import org.springframework.boot.context.properties.ConfigurationProperties;

/***
 * 需要使用到的spring的配置
 * @author way
 *
 */
@ConfigurationProperties(prefix="spring")
@SuppressWarnings("unchecked")
@Data
public class BootSpringConfig {
	private ProfilesConfig profiles = new ProfilesConfig();
	private ApplicationProperties application = new ApplicationProperties();
	
    public boolean isEnv(Env env){
		return Optional.ofNullable(profiles.getActive())
						.orElse(Collections.EMPTY_LIST)
						.contains(env.name().toLowerCase());
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
	
	@Data
	public class ProfilesConfig {
		private List<String> active;
	}
	
	@Data
	public class ApplicationProperties {
		String name;
	}
	
}
