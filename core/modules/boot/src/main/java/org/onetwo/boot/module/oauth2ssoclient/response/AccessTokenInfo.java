package org.onetwo.boot.module.oauth2ssoclient.response;

import java.io.Serializable;
import java.util.Date;

import lombok.Builder;
import lombok.Data;

/**
 * 必须有默认构造函数，json反序列化时需要
 * @author wayshall
 * <br/>
 */
@SuppressWarnings("serial")
@Data
public class AccessTokenInfo implements Serializable {

	private String accessToken;
	private Date expiration;

	public AccessTokenInfo() {
		super();
	}
	
	@Builder
	public AccessTokenInfo(String accessToken, Date expiration) {
		super();
		this.accessToken = accessToken;
		this.expiration = expiration;
	}

	public boolean isExpired() {
		return expiration != null && expiration.before(new Date());
	}

	public String getAccessToken() {
		return accessToken;
	}

}
