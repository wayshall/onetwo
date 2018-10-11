package org.onetwo.boot.module.oauth2.authorize;

import org.aopalliance.intercept.MethodInterceptor;
import org.springframework.security.oauth2.provider.client.ClientCredentialsTokenEndpointFilter;

/**
 * ClientCredentialsTokenEndpointFilter 增强标记接口
 * @author wayshall
 * <br/>
 */
public interface TokenEndpointFilterInterceptor extends MethodInterceptor {

}