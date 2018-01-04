package org.onetwo.boot.module.oauth2.clientdetails;

import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

/**
 * @author wayshall
 * <br/>
 */
public interface ClientDetailsObtainService {

	Optional<String> getTokenValue(HttpServletRequest request);

	Optional<Object> resolveAndStoreClientDetails(HttpServletRequest request);

	Object resolveClientDetails(String accessTokenValue);

}