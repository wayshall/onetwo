package org.onetwo.common.sso;

import java.util.Date;

@SuppressWarnings("serial")
public class SSOLastActivityInfo implements SSOLastActivityStatus {
	
	private Date lastActivityTime;
	private boolean login;
	
	public SSOLastActivityInfo(){
	}
	
	public Date getLastActivityTime() {
		return lastActivityTime;
	}
	public void setLastActivityTime(Date lastActivityTime) {
		this.lastActivityTime = lastActivityTime;
	}

	public boolean isLogin() {
		return login;
	}

	public void setLogin(boolean login) {
		this.login = login;
	}
	
	

}
