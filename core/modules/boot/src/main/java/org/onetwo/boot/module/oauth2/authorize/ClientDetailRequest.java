package org.onetwo.boot.module.oauth2.authorize;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

/**
 * @author wayshall
 * <br/>
 */
public interface ClientDetailRequest {

	String getClientId();
	String getClientSecret();
	String getClientCategory();
	
	@Data
	public class DefaultClientDetailRequest implements ClientDetailRequest {
		@JsonProperty("client_id")
		String clientId;
		@JsonProperty("client_secret")
		String clientSecret = "";
		@JsonProperty("client_category")
		String clientCategory;
	}

}
