package org.onetwo.boot.plugins.security.cas;

import org.jasig.cas.client.validation.Cas20ServiceTicketValidator;
import org.onetwo.boot.plugins.security.BootSecurityConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.cas.ServiceProperties;
import org.springframework.security.cas.authentication.CasAuthenticationProvider;
import org.springframework.security.cas.web.CasAuthenticationEntryPoint;
import org.springframework.security.cas.web.CasAuthenticationFilter;
import org.springframework.security.core.userdetails.UserDetailsByNameServiceWrapper;
import org.springframework.security.core.userdetails.UserDetailsService;

/***
 * @author way
 *
 */
@Configuration
//@Import(RbacSecurityXmlContextConfigSupport.class)
public class CasSsoContextConfig {
	
	@Autowired
	private BootSecurityConfig bootSecurityConfig;
	@Autowired
	private UserDetailsService userDetailsService;
	
	@Autowired
	private AuthenticationManager authenticationManager;

	@Bean
	@ConditionalOnMissingBean(ServiceProperties.class)
	public ServiceProperties serviceProperties(){
		ServiceProperties serviceProps = new ServiceProperties();
		serviceProps.setService(bootSecurityConfig.getCas().getService());
		serviceProps.setSendRenew(bootSecurityConfig.getCas().isSendRenew());
		return serviceProps;
	}
	
	@Bean
	@ConditionalOnMissingBean(CasAuthenticationEntryPoint.class)
	public CasAuthenticationEntryPoint casEntryPoint(){
		CasAuthenticationEntryPoint casEntryPoint = new CasAuthenticationEntryPoint();
		casEntryPoint.setServiceProperties(serviceProperties());
		casEntryPoint.setLoginUrl(bootSecurityConfig.getCas().getLoginUrl());
		return casEntryPoint;
	}
	
	@Bean
	@ConditionalOnMissingBean(CasAuthenticationProvider.class)
	public CasAuthenticationProvider casAuthenticationProvider(){
		CasAuthenticationProvider casProvider = new CasAuthenticationProvider();
		casProvider.setAuthenticationUserDetailsService(new UserDetailsByNameServiceWrapper<>(userDetailsService));
		casProvider.setServiceProperties(serviceProperties());
		casProvider.setTicketValidator(new Cas20ServiceTicketValidator(bootSecurityConfig.getCas().getCasServerUrl()));
		casProvider.setKey(bootSecurityConfig.getCas().getKey());
		return casProvider;
	}
	
	@Bean
	@ConditionalOnMissingBean(CasAuthenticationFilter.class)
	public CasAuthenticationFilter casFilter(){
		CasAuthenticationFilter casFilter = new CasAuthenticationFilter();
		casFilter.setAuthenticationManager(authenticationManager);
		return casFilter;
	}
	
	/*@Bean
	public CasSecurityConfigurerAdapter securityConfigurerAdapter(){
		return new CasSecurityConfigurerAdapter();
	}*/

}
