package org.onetwo.boot.module.oauth2.util;

import java.io.Serializable;
import java.util.Optional;
import java.util.function.Supplier;

import javax.servlet.http.HttpServletRequest;

import org.onetwo.common.web.utils.WebHolder;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
import org.springframework.security.oauth2.provider.authentication.TokenExtractor;
import org.springframework.util.Assert;

/**
 * @author wayshall
 * <br/>
 */
public abstract class OAuth2Utils {
	public static final String OAUTH2_CLIENT_DETAILS_SERVICE = "oauth2ClientDetailsService";
	
	private static final String CLIENT_DETAILS_ATTR_KEY = "__CLIENT_DETAILS__";

	public static <T extends Serializable> Optional<T> getCurrentClientDetails() {
		Optional<HttpServletRequest> req = WebHolder.getRequest();
		if(!req.isPresent()){
			return Optional.empty();
		}
		return getClientDetails(req.get());
	}
	public static <T extends Serializable> Optional<T> getClientDetails(HttpServletRequest request) {
		return getOrSetClientDetails(request, null);
	}
	
	@SuppressWarnings("unchecked")
	public static <T extends Serializable> Optional<T> getOrSetClientDetails(HttpServletRequest request, Supplier<T> supplier) {
		Object data = request.getAttribute(CLIENT_DETAILS_ATTR_KEY);
		if(data!=null){
			return Optional.of((T)data);
		}
		
		if(supplier==null){
			return Optional.empty();
		}
		
		T details = supplier.get();
		setCurrentClientDetails(request, details);
		return Optional.ofNullable(details);
	}
	
	public static void setCurrentClientDetails(HttpServletRequest request, Object clientDetail) {
		Assert.notNull(clientDetail);
		request.setAttribute(CLIENT_DETAILS_ATTR_KEY, clientDetail);
	}
	
	
	public static Optional<String> getAccessTokenValue(TokenExtractor tokenExtractor,  HttpServletRequest request){
		String accessToken = (String)request.getAttribute(OAuth2AuthenticationDetails.ACCESS_TOKEN_VALUE);
		if(accessToken==null){
			Authentication authentication = tokenExtractor.extract(request);
			accessToken = authentication==null?null:(String)authentication.getPrincipal();
		}
		return Optional.ofNullable(accessToken);
	}
	
	private OAuth2Utils(){
	}
	
}
