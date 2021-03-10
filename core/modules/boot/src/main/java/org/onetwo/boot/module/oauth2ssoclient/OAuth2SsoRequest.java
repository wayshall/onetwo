package org.onetwo.boot.module.oauth2ssoclient;

import org.hibernate.validator.constraints.NotBlank;

import lombok.Data;

/**
 * @author weishao zeng
 * <br/>
 */
@Data
public class OAuth2SsoRequest {
	@NotBlank(message = "clientId不能为空！")
	private String clientId;
	private String code;
	private String state;
	private String redirectUrl;

}

