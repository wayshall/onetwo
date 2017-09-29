package org.onetwo.boot.module.security.url;

import java.util.List;

import javax.sql.DataSource;

import org.onetwo.boot.module.security.BootSecurityConfig;
import org.onetwo.boot.module.security.config.BootSecurityCommonContextConfig;
import org.onetwo.ext.permission.api.PermissionConfig;
import org.onetwo.ext.security.DefaultUrlSecurityConfigurer;
import org.onetwo.ext.security.metadata.DatabaseSecurityMetadataSource;
import org.onetwo.ext.security.metadata.JdbcSecurityMetadataSourceBuilder;
import org.onetwo.ext.security.url.UrlBasedSecurityConfig;
import org.onetwo.ext.security.utils.SecurityConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.annotation.Order;
import org.springframework.security.access.AccessDecisionManager;

@Configuration
@Import(BootSecurityCommonContextConfig.class)
public class BootUrlBasedSecurityConfig extends UrlBasedSecurityConfig {
	@Autowired
	private SecurityConfig securityConfig;
	
	@ConditionalOnMissingBean(AccessDecisionManager.class)
	@Bean
	public AccessDecisionManager accessDecisionManager(){
		return super.accessDecisionManager();
	}
	
	@Bean
	@Order(SecurityProperties.ACCESS_OVERRIDE_ORDER)
	@ConditionalOnMissingBean(DefaultUrlSecurityConfigurer.class)
	@Autowired
	public DefaultUrlSecurityConfigurer defaultSecurityConfigurer(AccessDecisionManager accessDecisionManager){
		return super.defaultSecurityConfigurer(accessDecisionManager);
	}

	@ConditionalOnClass(name="org.springframework.jdbc.core.support.JdbcDaoSupport")//这里要字符串的形式，否则会抛错。且如果不抽取JdbcSecurityMetadataSourceBuilder接口隔离DatabaseSecurityMetadataSource, @ConditionalOnClass not work
	@ConditionalOnProperty(name=BootSecurityConfig.METADATA_SOURCE_KEY, prefix=BootSecurityConfig.SECURITY_PREFIX, havingValue=BootSecurityConfig.METADATA_SOURCE_DATABASE, matchIfMissing=true)
	@ConditionalOnBean(DataSource.class)
	@Bean
	@Autowired
	public JdbcSecurityMetadataSourceBuilder securityMetadataSource(DataSource dataSource, List<PermissionConfig<?>> configs){
		return super.securityMetadataSource(dataSource, configs);
	}
	
	
}
