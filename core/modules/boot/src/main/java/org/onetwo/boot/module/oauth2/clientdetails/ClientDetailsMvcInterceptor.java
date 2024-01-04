package org.onetwo.boot.module.oauth2.clientdetails;

import java.util.Optional;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.onetwo.boot.core.web.mvc.interceptor.MvcInterceptorAdapter;
import org.onetwo.boot.module.oauth2.JFishOauth2Properties.ClientDetailsResolverProps;
import org.onetwo.boot.module.oauth2.util.OAuth2Errors;
import org.onetwo.boot.module.oauth2.util.OAuth2Utils;
import org.onetwo.common.exception.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.web.method.HandlerMethod;

/**
 * @author wayshall
 * <br/>
 */
public class ClientDetailsMvcInterceptor extends MvcInterceptorAdapter {
	
	@Autowired(required = false)
	private ClientDetailsObtainService clientDetailsObtainService;
	@Autowired
	private ApplicationContext applicationContext;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, HandlerMethod handler) {
		boolean enabled = isEnabled();
		if (clientDetailsObtainService==null) {
			if (enabled) {
				throw new ServiceException("ClientDetailsObtainService not found!");
			} else {
				logger.warn("ClientDetailsObtainService not found!");
			}
			return true;
		}
		
		Optional<String> at = clientDetailsObtainService.getTokenValue(request);
		if(!at.isPresent()){
			throw new ServiceException(OAuth2Errors.CLIENT_ACCESS_TOKEN_NOT_FOUND);
		}
		OAuth2Utils.getOrSetClientDetails(request, ()->{
			return clientDetailsObtainService.resolveClientDetails(at.get());
		});
		return true;
	}

	@Override
	public int getOrder() {
		return afterFirst(ORDER_STEP);
	}
	

	protected boolean isEnabled(){
//		return new RelaxedPropertyResolver(applicationContext.getEnvironment()).getProperty(ClientDetailsResolverProps.ENABLED_KEY, Boolean.class, true);
		return applicationContext.getEnvironment().getProperty(ClientDetailsResolverProps.ENABLED_KEY, Boolean.class, true);
	}
}
