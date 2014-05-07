package org.onetwo.plugins.security.client.vo;

import org.hibernate.validator.constraints.NotBlank;

public class SsoLoginParams {
//	private String tokenName;
	@NotBlank
	private String tk;
	private String callback;

	public String getTk() {
		return tk;
	}

	public void setTk(String tk) {
		this.tk = tk;
	}

	public String getCallback() {
		return callback;
	}

	public void setCallback(String callback) {
		this.callback = callback;
	}
	
	
}
