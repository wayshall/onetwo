package org.onetwo.boot.module.oauth2ssoclient;

import java.io.Serializable;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.onetwo.boot.module.oauth2ssoclient.OAuth2SsoClientUserRepository.OAuth2SsoClientUser;
import org.springframework.util.Assert;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author wayshall
 * <br/>
 */
@SuppressWarnings("serial")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OAuth2SsoClientUserInfo implements Serializable, OAuth2SsoClientUser {
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
	private String openid;
	private String scope;

	@JsonProperty("user_info")
	private Map<String, Object> userInfo;

	private Long accessAt;
	private Long refreshAt;

	@JsonIgnore
	public boolean isAccessTokenExpired(){
		Assert.notNull(accessAt, "accessAt can not be null");
		long duration = System.currentTimeMillis() - accessAt;
		return TimeUnit.MILLISECONDS.toSeconds(duration) > expiresIn;
	}
	
	@JsonIgnore
	public boolean isRefreshToken(){
		if(refreshAt==null){
			return false;
		}
		long duration = System.currentTimeMillis() - refreshAt;
		return TimeUnit.MILLISECONDS.toDays(duration) > 30;
	}
	
}
