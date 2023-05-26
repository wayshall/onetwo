package org.onetwo.ext.security.jwt;

import org.onetwo.common.web.userdetails.GenericUserDetail;

import lombok.Builder;
import lombok.Data;

/**
 * @author wayshall
 * <br/>
 */
@Data
public class JwtSecurityTokenInfo {
	
	final private String token;
	final private String refreshToken;
	
	@Builder
	public JwtSecurityTokenInfo(String token, String refreshToken) {
		super();
		this.token = token;
		this.refreshToken = refreshToken;
	}


	private GenericUserDetail<?> userInfo;

}
