package org.onetwo.boot.core.jwt;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author wayshall
 * <br/>
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class JwtTokenInfo {
	
	private String token;
	private String refreshToken;

}
