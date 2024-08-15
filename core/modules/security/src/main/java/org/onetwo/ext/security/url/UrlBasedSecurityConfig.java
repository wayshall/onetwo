package org.onetwo.ext.security.url;

import java.util.Arrays;

import javax.sql.DataSource;

import org.onetwo.ext.security.base.ExtAffirmativeBased;
import org.onetwo.ext.security.config.SecurityCommonContextConfig;
import org.onetwo.ext.security.metadata.DatabaseSecurityMetadataSource;
import org.onetwo.ext.security.metadata.JdbcSecurityMetadataSourceBuilder;
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
	protected SecurityConfig securityConfig;
	
	@Bean
	public MultiWebExpressionVoter multiWebExpressionVoter(){
		return new MultiWebExpressionVoter(securityConfig);
	}
	
	@Bean
	public AccessDecisionManager accessDecisionManager(){
		AffirmativeBased affirmative = new ExtAffirmativeBased(Arrays.asList(multiWebExpressionVoter(), new WebExpressionVoter(), new AuthenticatedVoter()));
		return affirmative;
	}
	
	@Bean
	@Autowired
//	public JdbcSecurityMetadataSourceBuilder securityMetadataSource(DataSource dataSource, MenuInfoParserFactory<? extends IPermission> menuInfoParserFactory){
	public JdbcSecurityMetadataSourceBuilder securityMetadataSource(DataSource dataSource){
		DatabaseSecurityMetadataSource ms = new DatabaseSecurityMetadataSource();
		ms.setDataSource(dataSource);
		ms.setResourceQuery(securityConfig.getRbac().getResourceQuery());
		
//		List<? extends PermissionConfig<?>> configs = menuInfoParserFactory.getPermissionConfigList();
//		if(configs!=null){
//			List<String> appCodes = configs.stream().map(c->c.getAppCode()).collect(Collectors.toList());
//			ms.setAppCodes(appCodes);
//		}
		return ms;
	}
	
	@Bean
//	@Order(SecurityProperties.ACCESS_OV`ERRIDE_ORDER)
//	@ConditionalOnMissingBean(WebSecurityConfigurerAdapter.class)
	@Autowired
	public DefaultUrlSecurityConfigurer defaultSecurityConfigurer(AccessDecisionManager accessDecisionManager){
		return new DefaultUrlSecurityConfigurer(accessDecisionManager);
	}
}
