package org.onetwo.ext.security.jwt;

import lombok.Builder;
import lombok.Value;

/**
 * @author wayshall
 * <br/>
 */
@Value
@Builder
public class JwtTokenInfo {
	
	private String token;
	private String refreshToken;

}
