package org.onetwo.boot.module.oauth2.clientdetails;

import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.onetwo.boot.module.oauth2.clientdetails.OAuth2ClientDetails.TokenNotFoundActions;
import org.onetwo.boot.module.oauth2.util.OAuth2Errors;
import org.onetwo.boot.module.oauth2.util.OAuth2Utils;
import org.onetwo.common.exception.ServiceException;
import org.onetwo.common.spring.mvc.annotation.BootMvcArgumentResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.authentication.BearerTokenExtractor;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
import org.springframework.security.oauth2.provider.authentication.TokenExtractor;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

/**
 * 这个解释器只负责解释出clientDetail，不做任何验证检查，这些应该在网关层做
 * @author wayshall
 * <br/>
 */
@BootMvcArgumentResolver
public class ClientDetailsArgumentResolver implements HandlerMethodArgumentResolver {

	@Autowired
	private TokenStore tokenStore;
	private TokenExtractor tokenExtractor = new BearerTokenExtractor();
	private ClientDetailConverter clientDetailConverter;

	@Override
	public boolean supportsParameter(MethodParameter parameter) {
		return parameter.hasParameterAnnotation(OAuth2ClientDetails.class);
	}

	@Override
	public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
		HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest(HttpServletRequest.class);
		
		Optional<String> at = getAccessTokenValue(tokenExtractor, request);
		if(!at.isPresent()){
			OAuth2ClientDetails acd = parameter.getParameterAnnotation(OAuth2ClientDetails.class);
			if(acd.tokenNotFound()==TokenNotFoundActions.THROWS){
				throw new ServiceException(OAuth2Errors.CLIENT_ACCESS_TOKEN_NOT_FOUND);
			}
			return null;
		}
		Optional<Object> result = OAuth2Utils.getOrSetClientDetails(request, ()->resolveClientDetails(at.get()));
		return result.get();
	}

	protected Object resolveClientDetails(String accessTokenValue) {
//		OAuth2AccessToken accessToken = tokenStore.readAccessToken(accessTokenValue);
		OAuth2Authentication authentication = tokenStore.readAuthentication(accessTokenValue);
		if(clientDetailConverter!=null){
			return clientDetailConverter.convert(authentication);
		}
		return authentication;
	}
	
	static public Optional<String> getAccessTokenValue(TokenExtractor tokenExtractor,  HttpServletRequest request){
		String accessToken = (String)request.getAttribute(OAuth2AuthenticationDetails.ACCESS_TOKEN_VALUE);
		if(accessToken==null){
			Authentication authentication = tokenExtractor.extract(request);
			accessToken = authentication==null?null:(String)authentication.getPrincipal();
		}
		return Optional.ofNullable(accessToken);
	}

	public void setClientDetailConverter(ClientDetailConverter clientDetailConverter) {
		this.clientDetailConverter = clientDetailConverter;
	}
	

}
