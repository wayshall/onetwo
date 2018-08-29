package org.onetwo.cloud.sleuth;

import org.onetwo.dbm.core.spi.DbmInterceptor;
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
	@ConditionalOnClass(value=DbmInterceptor.class)
	public SleuthDbmInterceptor sleuthDbmInterceptor(){
		SleuthDbmInterceptor interceptor = new SleuthDbmInterceptor();
		return interceptor;
	}

}
