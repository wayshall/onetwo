package org.onetwo.common.sso;

import java.io.Serializable;

@SuppressWarnings("serial")
public class LogoutParams implements Serializable {
	
	private String clientCode;
	private String returnUrl;

	private boolean all;
	
	public String getReturnUrl() {
		return returnUrl;
	}
	public void setReturnUrl(String returnUrl) {
		this.returnUrl = returnUrl;
	}
	public String getClientCode() {
		return clientCode;
	}
	public void setClientCode(String clientCode) {
		this.clientCode = clientCode;
	}
	public boolean isAll() {
		return all;
	}
	public void setAll(boolean all) {
		this.all = all;
	}
	
}
