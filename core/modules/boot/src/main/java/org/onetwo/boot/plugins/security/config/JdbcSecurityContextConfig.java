package org.onetwo.boot.plugins.security.config;

import javax.sql.DataSource;

import org.onetwo.boot.plugins.security.DatabaseSecurityMetadataSource;
import org.onetwo.boot.plugins.security.RbacWebSecurityConfigurerAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@Import(SecurityCommonContextConfig.class)
public class JdbcSecurityContextConfig {
	

	/*@Autowired
	private ConversionService conversionService;*/
	
	@Bean
	@ConditionalOnMissingBean(WebSecurityConfigurerAdapter.class)
	public RbacWebSecurityConfigurerAdapter rbacWebSecurityConfigurerAdapter(){
		return new RbacWebSecurityConfigurerAdapter();
	}
	
	@Bean
	@Autowired
	@ConditionalOnBean(RbacWebSecurityConfigurerAdapter.class)
	public DatabaseSecurityMetadataSource securityMetadataSource(DataSource dataSource){
		DatabaseSecurityMetadataSource ms = new DatabaseSecurityMetadataSource();
		ms.setDataSource(dataSource);
		return ms;
	}
	
	
}
