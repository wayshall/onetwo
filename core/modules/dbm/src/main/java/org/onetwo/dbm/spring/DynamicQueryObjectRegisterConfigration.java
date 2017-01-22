package org.onetwo.dbm.spring;

import org.onetwo.common.db.dquery.AnnotationScanBasicDynamicQueryObjectRegisterTrigger;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DynamicQueryObjectRegisterConfigration implements ApplicationContextAware {

	private ApplicationContext applicationContext;
	
	@Bean
	public AnnotationScanBasicDynamicQueryObjectRegisterTrigger annotationScanBasicDynamicQueryObjectRegisterTrigger(){
		AnnotationScanBasicDynamicQueryObjectRegisterTrigger register = new AnnotationScanBasicDynamicQueryObjectRegisterTrigger(applicationContext);
		return register;
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		this.applicationContext = applicationContext;
	}
	
}
