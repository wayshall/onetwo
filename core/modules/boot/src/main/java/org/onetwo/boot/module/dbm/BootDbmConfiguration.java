package org.onetwo.boot.module.dbm;

import org.onetwo.dbm.mapping.DbmConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author wayshall
 * <br/>
 */
@Configuration
@EnableConfigurationProperties(BootDbmConfig.class)
public class BootDbmConfiguration {
	@Autowired 
	DbmConfig dbmConfig;
	
	@Bean
	public DbmConfig dbmConfig(){
		return dbmConfig;
	}

}
