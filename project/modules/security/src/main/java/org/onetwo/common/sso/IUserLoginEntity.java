package org.onetwo.common.sso;

import java.io.Serializable;
import java.util.Date;

import org.onetwo.common.db.IdEntity;


public interface IUserLoginEntity<T extends Serializable> extends IdEntity<T>{

	public T getUserId();
	public String getLoginName();
	public String getUserLoginToken();
	
	public boolean isUserLoging();
	public Date getLastlogTime();
	public void setLastlogTime(Date date);
	
	public void setLogoutTime(Date date);
	public void userLogout();
	public void unNormalUserLogout();

}
