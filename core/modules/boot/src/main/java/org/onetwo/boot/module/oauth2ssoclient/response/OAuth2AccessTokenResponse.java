package org.onetwo.boot.module.oauth2ssoclient.response;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author wayshall
 * <br/>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OAuth2AccessTokenResponse {
	/***
	 * 
	 */
	@JsonProperty("access_token")
	private String accessToken;
	/***
	 * 2个小时
	 */
	@JsonProperty("expires_in")
	private Long expiresIn;
	@JsonProperty("refresh_token")
	private String refreshToken;
	private String scope;

	@JsonProperty("user_info")
	private Map<String, Object> userInfo;
	
	/***
	 * bearer
	 */
	@JsonProperty("token_type")
	private String tokenType;

}
