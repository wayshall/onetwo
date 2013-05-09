package org.onetwo.common.sso;

import java.io.Serializable;
import java.util.Date;

public interface SSOLastActivityStatus extends Serializable{

	public Date getLastActivityTime(); 

	public boolean isLogin();

}