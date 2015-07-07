package org.onetwo.boot.plugins.security.support;

import javax.sql.DataSource;

import org.onetwo.boot.core.init.FixSecurityFilterBugServletContextInitializer;
import org.onetwo.boot.plugins.security.DatabaseSecurityMetadataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.security.SecurityAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RbacSecurityXmlContextConfigSupport {

	@Bean
	@Autowired
	public DatabaseSecurityMetadataSource securityMetadataSource(DataSource dataSource){
		DatabaseSecurityMetadataSource ms = new DatabaseSecurityMetadataSource();
		ms.setDataSource(dataSource);
		return ms;
	}
	
	@Bean
	public SecurityBeanPostProcessor securityBeanPostProcessor(){
		return new SecurityBeanPostProcessor();
	}
	
	@Bean
	@ConditionalOnMissingBean(SecurityAutoConfiguration.class)
	public FixSecurityFilterBugServletContextInitializer fixSecurityFilterBugServletContextInitializer(){
		return new FixSecurityFilterBugServletContextInitializer();
	}
}
