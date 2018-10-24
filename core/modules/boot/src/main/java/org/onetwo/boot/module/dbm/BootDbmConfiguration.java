package org.onetwo.boot.module.dbm;

import org.onetwo.dbm.core.spi.DbmSession;
import org.onetwo.dbm.mapping.DbmConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
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
	@Autowired 
	DbmConfig dbmConfig;
	
	@Bean
	public DbmConfig dbmConfig(){
		return dbmConfig;
	}
	
	@Bean
	@ConditionalOnProperty(name=BootDbmConfig.STATIS_ENABLED_KEY, matchIfMissing=false)
	public DbmStatisController dbmStatisController(){
		return new DbmStatisController();
	}

}
