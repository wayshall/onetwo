package org.onetwo.ext.security.config;

import org.onetwo.common.utils.LangUtils;
import org.onetwo.ext.security.utils.SecurityConfig;
import org.onetwo.ext.security.utils.SecurityConfig.StrictHttpFirewallConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.web.firewall.StrictHttpFirewall;

public class S6MethodWebSecurityConfigurer implements WebSecurityCustomizer {

	@Autowired
	protected SecurityConfig securityConfig;
	
	@Override
	public void customize(WebSecurity web) {
//      web.ignoring().antMatchers("/webjars/**", "/images/**", "/oauth/uncache_approvals", "/oauth/cache_approvals");
	  	//= new DefaultSecurityFilterChain(ignoredRequest) see: WebSecurity#performBuild
	  	if(securityConfig.isIgnoringDefautStaticPaths()){
//	  		web.ignoring().antMatchers("/webjars/**", "/images/**", "/static/**");
	  		web.ignoring().requestMatchers("/webjars/**", "/images/**", "/static/**");
	  	}
	  	if(!LangUtils.isEmpty(securityConfig.getIgnoringUrls())){
//	  		web.ignoring().antMatchers(securityConfig.getIgnoringUrls());
	  		web.ignoring().requestMatchers(securityConfig.getIgnoringUrls());
	  	}
	  	web.debug(securityConfig.isDebug()); 
	  	
	  	StrictHttpFirewallConfig strictHttpFirewall = securityConfig.getStrictHttpFirewall();
	  	if (strictHttpFirewall!=null) {
	      	StrictHttpFirewall httpFirewall = new StrictHttpFirewall();
	      	/****
	      	 * 是否允许url路径带有分号
	      	 */
	      	httpFirewall.setAllowSemicolon(strictHttpFirewall.isAllowSemicolon());
	      	web.httpFirewall(httpFirewall);
	  	}
	}

}
