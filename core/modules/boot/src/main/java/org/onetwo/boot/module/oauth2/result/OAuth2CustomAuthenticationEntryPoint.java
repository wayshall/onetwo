package org.onetwo.boot.module.oauth2.result;

import lombok.extern.slf4j.Slf4j;

import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;
import org.springframework.security.oauth2.provider.error.OAuth2AuthenticationEntryPoint;

/**
 * @author wayshall
 * <br/>
 */
@Slf4j
public class OAuth2CustomAuthenticationEntryPoint extends OAuth2AuthenticationEntryPoint {

	@Override
	protected ResponseEntity<OAuth2Exception> enhanceResponse(ResponseEntity<OAuth2Exception> response, Exception exception) {
		if(log.isInfoEnabled()){
			log.error("oauth2 error", exception);
		}
		return super.enhanceResponse(response, exception);
	}
}
