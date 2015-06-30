package org.onetwo.boot.core.config;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.onetwo.common.utils.propconf.Env;
import org.springframework.boot.context.properties.ConfigurationProperties;

/***
 * 需要使用到的spring的配置
 * @author way
 *
 */
@ConfigurationProperties(prefix="spring")
@SuppressWarnings("unchecked")
public class SpringBootConfig {
	private List<String> profilesActive;
	

    public boolean isEnv(Env env){
		return Optional.ofNullable(profilesActive)
						.orElse(Collections.EMPTY_LIST)
						.contains(env.name());
	}
	
	public boolean isProduct(){
		return isEnv(Env.PRODUCT);
	}
	
}
