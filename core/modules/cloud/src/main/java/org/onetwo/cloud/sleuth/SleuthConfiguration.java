package org.onetwo.cloud.sleuth;

import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.cloud.sleuth.Tracer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author wayshall
 * <br/>
 */
@Configuration
@ConditionalOnClass(value=Tracer.class)
public class SleuthConfiguration {

	@Bean
	@ConditionalOnClass(name="org.onetwo.dbm.core.spi.DbmInterceptor")
	public SleuthDbmInterceptor sleuthDbmInterceptor(){
		SleuthDbmInterceptor interceptor = new SleuthDbmInterceptor();
		return interceptor;
	}
	
	@Bean
	@ConditionalOnClass(name="org.onetwo.ext.ons.producer.ONSProducerServiceImpl")
	@ConditionalOnBean(type="org.onetwo.ext.ons.producer.ONSProducerServiceImpl")
	public SleuthRocketmqInterceptor SleuthRocketmqInterceptor() {
		return new SleuthRocketmqInterceptor();
	}

}
