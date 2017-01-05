package org.onetwo.ext.security.url;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.sql.DataSource;

import org.onetwo.ext.permission.api.PermissionConfig;
import org.onetwo.ext.security.DatabaseSecurityMetadataSource;
import org.onetwo.ext.security.DefaultUrlSecurityConfigurer;
import org.onetwo.ext.security.config.SecurityCommonContextConfig;
import org.onetwo.ext.security.utils.SecurityConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.vote.AffirmativeBased;
import org.springframework.security.access.vote.AuthenticatedVoter;
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
	@Autowired
	private SecurityConfig securityConfig;
	
	@Bean
	public MultiWebExpressionVoter multiWebExpressionVoter(){
		return new MultiWebExpressionVoter();
	}
	
	@Bean
	public AccessDecisionManager accessDecisionManager(){
		AffirmativeBased affirmative = new AffirmativeBased(Arrays.asList(multiWebExpressionVoter(), new WebExpressionVoter(), new AuthenticatedVoter()));
		return affirmative;
	}
	
	@Bean
	@Autowired
	public DatabaseSecurityMetadataSource securityMetadataSource(DataSource dataSource, List<PermissionConfig<?>> configs){
		DatabaseSecurityMetadataSource ms = new DatabaseSecurityMetadataSource();
		ms.setDataSource(dataSource);
		ms.setResourceQuery(securityConfig.getRbac().getResourceQuery());
		if(configs!=null){
			List<String> appCodes = configs.stream().map(c->c.getAppCode()).collect(Collectors.toList());
			ms.setAppCodes(appCodes);
		}
		return ms;
	}
	
	@Bean
//	@Order(SecurityProperties.ACCESS_OVERRIDE_ORDER)
//	@ConditionalOnMissingBean(WebSecurityConfigurerAdapter.class)
	@Autowired
	public DefaultUrlSecurityConfigurer defaultSecurityConfigurer(AccessDecisionManager accessDecisionManager){
		return new DefaultUrlSecurityConfigurer(accessDecisionManager);
	}
}
