package org.onetwo.common.sso;

import java.io.Serializable;

import org.onetwo.common.db.IdEntity;


public interface IUserEntity<T extends Serializable> extends IdEntity<T>{

	public String getUserAccount();

	public String getUserPassword();
	
	public boolean isUserAvailable();
}
