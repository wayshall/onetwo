package org.onetwo.ext.security.jwt;

import lombok.Builder;
import lombok.Value;

/**
 * @author wayshall
 * <br/>
 */
@Value
@Builder
public class JwtSecurityTokenInfo {
	
	private String token;
	private String refreshToken;

}
