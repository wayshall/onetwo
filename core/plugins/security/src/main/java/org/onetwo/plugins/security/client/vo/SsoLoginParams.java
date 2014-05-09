package org.onetwo.plugins.security.client.vo;


public class SsoLoginParams {
//	private String tokenName;
	private String tk;
	private String callback;
	private String sign;

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

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}
	
	
}
