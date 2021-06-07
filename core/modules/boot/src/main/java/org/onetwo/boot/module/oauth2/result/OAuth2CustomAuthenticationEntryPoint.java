package org.onetwo.boot.module.oauth2.result;

import org.onetwo.common.web.utils.WebHolder;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;
import org.springframework.security.oauth2.provider.authentication.BearerTokenExtractor;
import org.springframework.security.oauth2.provider.authentication.TokenExtractor;
import org.springframework.security.oauth2.provider.error.OAuth2AuthenticationEntryPoint;

import lombok.extern.slf4j.Slf4j;


/**
 * @author wayshall
 * <br/>
 */
@Slf4j
public class OAuth2CustomAuthenticationEntryPoint extends OAuth2AuthenticationEntryPoint implements InitializingBean {

	@Autowired(required=false)
	private TokenExtractor tokenExtractor;
//	private TokenExtractor tokenExtractor = new BearerTokenExtractor();
	
	@Override
	protected ResponseEntity<OAuth2Exception> enhanceResponse(ResponseEntity<OAuth2Exception> response, Exception exception) {
		if(log.isErrorEnabled()){
			WebHolder.getRequest().ifPresent(request->{
				Authentication auth = tokenExtractor.extract(request);
				log.error("token:{}, auth: {}", auth==null?"null":auth.getPrincipal(), auth);
			});
			log.error("oauth2 error", exception);
		}
		return super.enhanceResponse(response, exception);
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		if (tokenExtractor==null) {
			tokenExtractor = new BearerTokenExtractor();
		}
	}
}
