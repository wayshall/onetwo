package org.onetwo.boot.module.security;

import org.onetwo.common.utils.StringUtils;
import org.onetwo.ext.security.utils.SecurityConfig.CookieConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.session.Session;
import org.springframework.session.web.http.CookieSerializer;
import org.springframework.session.web.http.DefaultCookieSerializer;

/**
 * @author wayshall
 * <br/>
 */
@Configuration
@ConditionalOnClass(Session.class)
@EnableConfigurationProperties(BootSecurityConfig.class)
public class FixSpringSessionCookieSerializerConfiguration {
	
	@Autowired
	private BootSecurityConfig securityConfig;
	
	@Bean
	@ConditionalOnMissingBean(CookieSerializer.class)
	public CookieSerializer cookieSerializer(){
//		DefaultCookieSerializer serializer = new DefaultCookieSerializer();
		DefaultCookieSerializer serializer;
		if (securityConfig.getCookie().isFixCookie()) {
			serializer = new FixCookieSerializer();
		} else {
			serializer = new DefaultCookieSerializer();
		}
		CookieConfig cookieConfig = securityConfig.getCookie();
//		if(StringUtils.isNotBlank(cookieConfig.getPath())){
		serializer.setCookiePath(cookieConfig.getPath());
//		}
		if(StringUtils.isNotBlank(cookieConfig.getDomain())){
			serializer.setDomainName(cookieConfig.getDomain());
		}
		serializer.setCookieName(cookieConfig.getName());
		return serializer;
	}

}
