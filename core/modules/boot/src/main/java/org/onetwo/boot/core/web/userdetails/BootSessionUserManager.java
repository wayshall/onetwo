package org.onetwo.boot.core.web.userdetails;

import org.onetwo.boot.core.web.utils.BootWebUtils;
import org.onetwo.common.web.userdetails.GenericUserDetail;
import org.onetwo.common.web.userdetails.SessionUserManager;

public class BootSessionUserManager implements SessionUserManager<GenericUserDetail<?>> {

	@Override
	public GenericUserDetail<?> getCurrentUser() {
		return BootWebUtils.getUserDetail();
	}
	
	

}
