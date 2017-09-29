package org.onetwo.ext.security.metadata;

import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;

/**
 * @author wayshall
 * <br/>
 */
public interface SecurityMetadataSourceBuilder {
	
	void setFilterSecurityInterceptor(FilterSecurityInterceptor filterSecurityInterceptor);
	
	void buildSecurityMetadataSource();

}
