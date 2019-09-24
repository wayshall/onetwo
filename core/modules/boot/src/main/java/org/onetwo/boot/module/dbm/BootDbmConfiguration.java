package org.onetwo.boot.module.dbm;

import org.onetwo.dbm.core.spi.DbmSession;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author wayshall
 * <br/>
 */
@ConditionalOnClass(value=DbmSession.class)
//@ConditionalOnBean(value=DbmSessionFactory.class)
@Configuration
@EnableConfigurationProperties(BootDbmConfig.class)
public class BootDbmConfiguration {
	/*@Autowired 
	DbmConfig dbmConfig;
	
	@Bean
	public DbmConfig dbmConfig(){
		return dbmConfig;
	}*/
	
}
