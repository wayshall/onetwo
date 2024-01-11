package org.onetwo.boot.module.security.url;

import javax.sql.DataSource;

import org.onetwo.boot.module.security.BootSecurityConfig;
import org.onetwo.boot.module.security.config.BootSecurityCommonContextConfig;
import org.onetwo.ext.security.metadata.JdbcSecurityMetadataSourceBuilder;
import org.onetwo.ext.security.provider.ExceptionUserChecker;
import org.onetwo.ext.security.url.UrlBasedSecurityConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(BootSecurityCommonContextConfig.class)
public class BootUrlBasedSecurityConfig extends UrlBasedSecurityConfig {
	@Autowired
	private BootSecurityConfig securityConfig;
	
	@ConditionalOnClass(name="org.springframework.jdbc.core.support.JdbcDaoSupport")//这里要字符串的形式，否则会抛错。且如果不抽取JdbcSecurityMetadataSourceBuilder接口隔离DatabaseSecurityMetadataSource, @ConditionalOnClass not work
	@ConditionalOnProperty(name=BootSecurityConfig.METADATA_SOURCE_KEY, prefix=BootSecurityConfig.SECURITY_PREFIX, havingValue=BootSecurityConfig.METADATA_SOURCE_DATABASE, matchIfMissing=true)
	@Bean
	@Autowired
	public JdbcSecurityMetadataSourceBuilder securityMetadataSource(DataSource dataSource){
		return super.securityMetadataSource(dataSource);
	}
	
	@Bean
	@ConditionalOnProperty(name=BootSecurityConfig.EXCEPTION_USER_CHECKER_ENABLE_KEY, matchIfMissing=true)
	public ExceptionUserChecker exceptionUserChecker(){
		ExceptionUserChecker checker = new ExceptionUserChecker();
		checker.setExceptionUserCheckerConfig(securityConfig.getExceptionUserChecker());
//		checker.setMaxLoginTimes(securityConfig.getExceptionUserChecker().getMaxLoginTimes());
		return checker;
	}
	
}
