package org.onetwo.boot.plugins.dbm;

import org.onetwo.common.db.dquery.DynamicQueryObjectRegisterListener;
import org.onetwo.common.jfishdbm.spring.JFishdbmSpringConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@ConditionalOnClass(JFishdbmSpringConfiguration.class)
@Import({JFishdbmSpringConfiguration.class})
@AutoConfigureAfter(DataSourceAutoConfiguration.class)
public class DbmContextAutoConfig {
	
	@Bean
	public DynamicQueryObjectRegisterListener dynamicQueryObjectRegisterListener(){
		return new DynamicQueryObjectRegisterListener();
	}
}
