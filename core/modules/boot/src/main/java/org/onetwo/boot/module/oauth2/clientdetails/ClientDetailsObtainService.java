package org.onetwo.boot.module.oauth2.clientdetails;

import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

/**
 * @author wayshall
 * <br/>
 */
public interface ClientDetailsObtainService {

	Optional<String> getTokenValue(HttpServletRequest request);

	Optional<? extends ClientDetails> resolveAndStoreClientDetails(HttpServletRequest request);

	ClientDetails resolveClientDetails(String accessTokenValue);

}