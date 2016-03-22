package org.onetwo.ext.security.url;

import javax.sql.DataSource;

import org.onetwo.ext.security.DatabaseSecurityMetadataSource;
import org.onetwo.ext.security.DefaultUrlSecurityConfigurer;
import org.onetwo.ext.security.config.SecurityCommonContextConfig;
import org.onetwo.ext.security.method.DefaultMethodSecurityConfigurer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

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
	@Autowired
	public DatabaseSecurityMetadataSource securityMetadataSource(DataSource dataSource){
		DatabaseSecurityMetadataSource ms = new DatabaseSecurityMetadataSource();
		ms.setDataSource(dataSource);
		return ms;
	}
	
	@Bean
//	@Order(SecurityProperties.ACCESS_OVERRIDE_ORDER)
//	@ConditionalOnMissingBean(WebSecurityConfigurerAdapter.class)
	public DefaultMethodSecurityConfigurer defaultSecurityConfigurer(){
		return new DefaultUrlSecurityConfigurer();
	}
}
