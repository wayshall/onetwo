package org.onetwo.boot.core.web.mvc;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;

/**
 * @author wayshall
 * <br/>
 */
public class EnhanceRequestMappingConfiguration {

	/****
	 * 扩展requestmapping，增加监听器
	 * @author wayshall
	 * @return
	 */
	@Bean
	public RequestMappingHandlerMappingListenable requestMappingHandlerMappingListenable(){
		RequestMappingHandlerMappingListenable req = new RequestMappingHandlerMappingListenable();
		return req;	
	}
	
	@Bean
	@ConditionalOnMissingBean(BootWebMvcRegistrations.class)
	public BootWebMvcRegistrations bootWebMvcRegistrations(){
		return new BootWebMvcRegistrations();
	}
}
