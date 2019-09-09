package org.onetwo.boot.module.oauth2.restclient;

import org.onetwo.boot.module.oauth2.JFishOauth2Properties.ClientDetailsResolverProps;
import org.onetwo.boot.module.oauth2.util.OAuth2Utils;
import org.onetwo.common.annotation.FieldName;
import org.onetwo.common.apiclient.annotation.RestApiClient;
import org.onetwo.common.data.AbstractDataResult;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @author weishao zeng
 * <br/>
 */
@RestApiClient(url="${"+ClientDetailsResolverProps.AUTHORIZATION_BASE_URL+"}")
public interface OAuth2TokenClient {
	
	@PostMapping(path="/oauth/token", consumes=MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	GetOAuth2TokenResponse getAccessToken(GetOAuth2TokenRequest request);

	@Data
	@Builder
	@NoArgsConstructor
	@AllArgsConstructor
	public class GetOAuth2TokenRequest {
		@Builder.Default
		@FieldName("grant_type")
		String grantType = OAuth2Utils.GRANT_TYPE_CLIENT_CREDENTIALS;
		@FieldName("client_id")
		String clientId;
		@FieldName("client_secret")
		String clientSecret;
	}
	
	@SuppressWarnings("serial")
	@Data
	@EqualsAndHashCode(callSuper=true)
	public class GetOAuth2TokenResponse extends AbstractDataResult<Auth2TokenResult> {
		private Auth2TokenResult data;
		
		public Auth2TokenResult getData() {
			return data;
		}
	}
	
	@Data
	public class Auth2TokenResult {
		@JsonProperty("access_token")
		String accessToken;
		@JsonProperty("expiresIn")
		Long expires_in;
	}
}

