package org.onetwo.ext.security.metadata;

import java.util.Collection;

import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;

/**
 * @author wayshall
 * <br/>
 */
public interface JdbcSecurityMetadataSourceBuilder {
	
	void buildSecurityMetadataSource();
	
	Collection<ConfigAttribute> getAttributes(RequestAuthorizationContext context);
	
}
