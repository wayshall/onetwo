package org.onetwo.boot.module.oauth2.authorize;

import lombok.Data;

/**
 * @author wayshall
 * <br/>
 */
public interface ClientDetailRequest {

	String getClientId();
	String getClientSecret();
	
	@Data
	public class DefaultClientDetailRequest implements ClientDetailRequest {
		String clientId;
		String clientSecret;
	}

}
