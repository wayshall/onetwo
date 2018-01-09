package org.onetwo.boot.module.oauth2.clientdetails;

import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.onetwo.boot.core.web.mvc.interceptor.MvcInterceptorAdapter;
import org.onetwo.boot.module.oauth2.util.OAuth2Errors;
import org.onetwo.boot.module.oauth2.util.OAuth2Utils;
import org.onetwo.common.exception.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.method.HandlerMethod;

/**
 * @author wayshall
 * <br/>
 */
public class ClientDetailsMvcInterceptor extends MvcInterceptorAdapter {
	
	@Autowired
	private ClientDetailsObtainService clientDetailsObtainService;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, HandlerMethod handler) {
		Optional<String> at = clientDetailsObtainService.getTokenValue(request);
		if(!at.isPresent()){
			throw new ServiceException(OAuth2Errors.CLIENT_ACCESS_TOKEN_NOT_FOUND);
		}
		OAuth2Utils.getOrSetClientDetails(request, ()->{
			return clientDetailsObtainService.resolveClientDetails(at.get());
		});
		return true;
	}
	
}
