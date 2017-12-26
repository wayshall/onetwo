package org.onetwo.boot.module.oauth2.clientdetails;

import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.onetwo.boot.core.web.mvc.interceptor.MvcInterceptorAdapter;
import org.onetwo.boot.module.oauth2.util.OAuth2Errors;
import org.onetwo.common.exception.ServiceException;
import org.onetwo.common.jackson.JsonMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.provider.authentication.BearerTokenExtractor;
import org.springframework.security.oauth2.provider.authentication.TokenExtractor;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.web.method.HandlerMethod;

/**
 * @author wayshall
 * <br/>
 */
public class ClientDetailsMvcInterceptor extends MvcInterceptorAdapter {
	public static final String CLIENT_DETAILS_ATTR_KEY = "__CLIENT_DETAILS__";
	
	@Autowired
	private TokenStore tokenStore;
	private TokenExtractor tokenExtractor = new BearerTokenExtractor();

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, HandlerMethod handler) {
		//TODO
		Optional<String> at = null;// getAccessTokenValue(request);
		if(!at.isPresent()){
			throw new ServiceException(OAuth2Errors.CLIENT_ACCESS_TOKEN_NOT_FOUND);
		}
//		tokenStore.readAccessToken(tokenValue);
		return true;
	}
	
	/*public Optional<String> getAccessTokenValue(HttpServletRequest request){
		String accessToken = (String)request.getAttribute(OAuth2AuthenticationDetails.ACCESS_TOKEN_VALUE);
		if(accessToken==null){
			Authentication authentication = tokenExtractor.extract(request);
			accessToken = authentication==null?null:(String)authentication.getPrincipal();
		}
		return Optional.ofNullable(accessToken);
	}*/

	public static Optional<ClientDetails> getOrSetClientDetails(HttpServletRequest request, String authHeaderName) {
		Object data = request.getAttribute(CLIENT_DETAILS_ATTR_KEY);
		if(data instanceof ClientDetails){
			return Optional.of((ClientDetails)data);
		}
		String token = request.getHeader(authHeaderName);

		if(StringUtils.isBlank(token)){
			return Optional.empty();
		}
		
		ClientDetails details = JsonMapper.IGNORE_EMPTY.fromJson(token, ClientDetails.class);
		request.setAttribute(CLIENT_DETAILS_ATTR_KEY, details);
		return Optional.ofNullable(details);
	}

}
