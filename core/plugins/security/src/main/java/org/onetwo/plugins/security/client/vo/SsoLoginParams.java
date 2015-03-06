package org.onetwo.plugins.security.client.vo;


public class SsoLoginParams {
	public static final String PARAMS_SESSION_ID = "sid";
	public static final String PARAMS_TK = "tk";
//	private String tokenName;
	private String sid;
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

	public String getSid() {
		return sid;
	}

	public void setSid(String sid) {
		this.sid = sid;
	}

	
	
}
