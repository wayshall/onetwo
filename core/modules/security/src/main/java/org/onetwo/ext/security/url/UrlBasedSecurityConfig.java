package org.onetwo.ext.security.url;

import java.util.Arrays;

import javax.sql.DataSource;

import org.onetwo.ext.security.DatabaseSecurityMetadataSource;
import org.onetwo.ext.security.DefaultUrlSecurityConfigurer;
import org.onetwo.ext.security.config.SecurityCommonContextConfig;
import org.onetwo.ext.security.method.DefaultMethodSecurityConfigurer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.vote.AffirmativeBased;
import org.springframework.security.web.access.expression.WebExpressionVoter;

/***
 * 配置由url特有的配置+common配置组成
 * common继承SecurityCommonContextConfig
 * @author way
 *
 */
@Configuration
@Import(SecurityCommonContextConfig.class)
public class UrlBasedSecurityConfig {
	/***
	 * 如果不是基于方法拦截（即url匹配），需要用后处理器重新配置SecurityMetadataSource
	 * @return
	 */
	@Bean
	public SecurityBeanPostProcessor securityBeanPostProcessor(){
		return new SecurityBeanPostProcessor();
	}

	
	@Bean
	public MultiWebExpressionVoter multiWebExpressionVoter(){
		return new MultiWebExpressionVoter();
	}
	
	@Bean
	public AccessDecisionManager accessDecisionManager(){
		AffirmativeBased affirmative = new AffirmativeBased(Arrays.asList(multiWebExpressionVoter(), new WebExpressionVoter()));
		return affirmative;
	}
	
	@Bean
	@Autowired
	public DatabaseSecurityMetadataSource securityMetadataSource(DataSource dataSource){
		DatabaseSecurityMetadataSource ms = new DatabaseSecurityMetadataSource();
		ms.setDataSource(dataSource);
		return ms;
	}
	
	@Bean
//	@Order(SecurityProperties.ACCESS_OVERRIDE_ORDER)
//	@ConditionalOnMissingBean(WebSecurityConfigurerAdapter.class)
	@Autowired
	public DefaultMethodSecurityConfigurer defaultSecurityConfigurer(AccessDecisionManager accessDecisionManager){
		return new DefaultUrlSecurityConfigurer(accessDecisionManager);
	}
}
