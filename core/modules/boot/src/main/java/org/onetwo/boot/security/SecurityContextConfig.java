package org.onetwo.boot.security;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SecurityContextConfig {
	

	@Bean
	@Autowired
	@ConditionalOnBean(RbacWebSecurityConfigurerAdapter.class)
	public DatabaseSecurityMetadataSource securityMetadataSource(DataSource dataSource){
		DatabaseSecurityMetadataSource ms = new DatabaseSecurityMetadataSource();
		ms.setDataSource(dataSource);
		return ms;
	}
	
}
