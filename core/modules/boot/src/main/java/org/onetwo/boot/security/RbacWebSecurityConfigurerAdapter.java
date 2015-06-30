package org.onetwo.boot.security;

import javax.annotation.Resource;

import org.springframework.security.config.annotation.ObjectPostProcessor;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;

public class RbacWebSecurityConfigurerAdapter extends WebSecurityConfigurerAdapter  {

	@Resource
	private FilterInvocationSecurityMetadataSource securityMetadataSource;
	
	@Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
	    super.configure(auth);
    }

	@Override
    public void configure(WebSecurity web) throws Exception {
	    super.configure(web);
    }

	@Override
    protected void configure(HttpSecurity http) throws Exception {
		//DefaultFilterInvocationSecurityMetadataSource
	    super.configure(http);
	    http
	    	.authorizeRequests()
	    		.anyRequest().authenticated()
	    		.withObjectPostProcessor(new ObjectPostProcessor<FilterSecurityInterceptor>() {

					@Override
                    public <O extends FilterSecurityInterceptor> O postProcess(O object) {
	                    object.setSecurityMetadataSource(securityMetadataSource);
	                    return object;
                    }
	    			
				});
    }
	
	

}
