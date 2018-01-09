package org.onetwo.boot.module.oauth2.clientdetails;

import java.io.Serializable;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

/**
 * @author wayshall
 * <br/>
 */
public interface ClientDetailsObtainService {

	Optional<String> getTokenValue(HttpServletRequest request);

	Optional<? extends Serializable> resolveAndStoreClientDetails(HttpServletRequest request);

	Serializable resolveClientDetails(String accessTokenValue);

}