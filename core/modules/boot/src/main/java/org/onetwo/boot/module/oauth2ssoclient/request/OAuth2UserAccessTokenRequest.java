package org.onetwo.boot.module.oauth2ssoclient.request;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.validator.constraints.NotBlank;
import org.onetwo.boot.module.oauth2ssoclient.Oauth2SsoClients;
import org.onetwo.common.annotation.FieldName;

import lombok.Builder;
import lombok.Data;

/**
 * @author wayshall
 * <br/>
 */
@Data
//@EqualsAndHashCode(callSuper=false)
public class OAuth2UserAccessTokenRequest {
	
	@NotBlank
	private String clientId;
	@NotBlank
	private String secret;
	/***
	 * 填写第一步获取的code参数
	 */
	@NotBlank
	private String code;
	@NotBlank
	@FieldName("grant_type")
	private String grantType;

	@Builder
	public OAuth2UserAccessTokenRequest(String clientId, String secret, String code, String grantType) {
		this.clientId = clientId;
		this.code = code;
		if(StringUtils.isBlank(grantType)){
			this.grantType = Oauth2SsoClients.AUTHORIZATION_CODE;
		}else{
			this.grantType = grantType;
		}
	}
	
	

}
