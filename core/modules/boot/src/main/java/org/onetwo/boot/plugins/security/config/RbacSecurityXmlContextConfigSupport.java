package org.onetwo.boot.plugins.security.config;

import javax.sql.DataSource;

import org.onetwo.boot.core.init.FixSecurityFilterBugServletContextInitializer;
import org.onetwo.ext.security.DatabaseSecurityMetadataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.security.SecurityAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(SecurityCommonContextConfig.class)
public class RbacSecurityXmlContextConfigSupport {

	@Bean
	@Autowired
	public DatabaseSecurityMetadataSource securityMetadataSource(DataSource dataSource){
		DatabaseSecurityMetadataSource ms = new DatabaseSecurityMetadataSource();
		ms.setDataSource(dataSource);
		return ms;
	}
	
	/****
	 * boot1.2.x 不启动security的autoconfig时，有bug，会自动注册不受控的filter
	 * @return
	 */
	@Bean
	@ConditionalOnMissingBean(SecurityAutoConfiguration.class)
	public FixSecurityFilterBugServletContextInitializer fixSecurityFilterBugServletContextInitializer(){
		return new FixSecurityFilterBugServletContextInitializer();
	}
	
	
}
