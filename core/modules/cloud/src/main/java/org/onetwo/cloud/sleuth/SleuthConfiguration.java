package org.onetwo.cloud.sleuth;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import brave.Tracer;
import brave.Tracing;

/**
 * @author wayshall
 * <br/>
 */
@Configuration
@ConditionalOnClass(value=Tracer.class)
public class SleuthConfiguration {

	@Bean
	@ConditionalOnClass(name="org.onetwo.dbm.core.spi.DbmInterceptor")
	public SleuthDbmInterceptor sleuthDbmInterceptor(Tracing tracing){
		SleuthDbmInterceptor interceptor = new SleuthDbmInterceptor(tracing);
		return interceptor;
	}

}
