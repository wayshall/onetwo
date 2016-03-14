package org.onetwo.boot.core.web.userdetails;

import org.onetwo.boot.core.web.utils.BootWebUtils;
import org.onetwo.common.web.userdetails.SessionUserManager;
import org.onetwo.common.web.userdetails.UserDetail;

public class BootSessionUserManager implements SessionUserManager<UserDetail> {

	@Override
	public UserDetail getCurrentUser() {
		return BootWebUtils.getUserDetail();
	}
	
	

}
