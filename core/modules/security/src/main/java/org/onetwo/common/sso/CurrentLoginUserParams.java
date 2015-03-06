package org.onetwo.common.sso;

import java.io.Serializable;

public class CurrentLoginUserParams implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3051608134041739789L;
	private String token;
	private String sign;
	private String clientCode;
	
	public CurrentLoginUserParams(String token) {
		super();
		this.token = token;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public String getSign() {
		return sign;
	}
	public void setSign(String sign) {
		this.sign = sign;
	}
	public String getClientCode() {
		return clientCode;
	}
	public void setClientCode(String clientCode) {
		this.clientCode = clientCode;
	}
	
}
