package org.onetwo.boot.module.oauth2ssoclient;

import org.onetwo.common.annotation.FieldName;
import org.onetwo.common.utils.ParamUtils;

import lombok.Builder;
import lombok.Data;

/**
 * @author wayshall
 * <br/>
 */
@Data
@Builder
public class OAuth2SsoClientAuthorizeData {
	
	private String appid;
	@FieldName("redirect_uri")
	private String redirectUri;
	@FieldName("response_type")
	private String responseType;
	private String scope;
	private String state;
	
	/***
	 * http://server:port/oauth/authorize
	 */
	private String userAuthorizationUri;
	
	public String toAuthorizeUrl(){
		String url = userAuthorizationUri + "?" + ParamUtils.objectToParamString(this);
		return url;
	}

}
