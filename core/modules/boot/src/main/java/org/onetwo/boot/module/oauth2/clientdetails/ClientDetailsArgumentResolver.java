package org.onetwo.boot.module.oauth2.clientdetails;

import java.io.Serializable;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.onetwo.boot.module.oauth2.annotation.OAuth2ClientDetails;
import org.onetwo.boot.module.oauth2.annotation.OAuth2ClientDetails.TokenNotFoundActions;
import org.onetwo.boot.module.oauth2.util.OAuth2Errors;
import org.onetwo.common.exception.ServiceException;
import org.onetwo.common.spring.mvc.annotation.BootMvcArgumentResolver;
import org.springframework.core.MethodParameter;
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

	private ClientDetailsObtainService clientDetailsObtainService;

	@Override
	public boolean supportsParameter(MethodParameter parameter) {
		return parameter.hasParameterAnnotation(OAuth2ClientDetails.class);
	}

	@Override
	public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
		HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest(HttpServletRequest.class);
		
		Optional<String> at = clientDetailsObtainService.getTokenValue(request);
		if(!at.isPresent()){
			OAuth2ClientDetails acd = parameter.getParameterAnnotation(OAuth2ClientDetails.class);
			if(acd.tokenNotFound()==TokenNotFoundActions.THROWS){
				throw new ServiceException(OAuth2Errors.CLIENT_ACCESS_TOKEN_NOT_FOUND);
			}
			return null;
		}
		Optional<? extends Serializable> result = clientDetailsObtainService.resolveAndStoreClientDetails(request);
		return result.orElse(null);
	}

	public void setClientDetailsObtainService(
			ClientDetailsObtainService clientDetailsObtainService) {
		this.clientDetailsObtainService = clientDetailsObtainService;
	}


}
