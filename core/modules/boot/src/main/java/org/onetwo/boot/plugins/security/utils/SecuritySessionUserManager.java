package org.onetwo.boot.plugins.security.utils;

import org.onetwo.common.web.userdetails.SessionUserManager;
import org.onetwo.common.web.userdetails.UserDetail;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class SecuritySessionUserManager implements SessionUserManager<UserDetail> {

	@Override
	public UserDetail getCurrentUser() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if(auth==null)
			return null;
		if(UserDetail.class.isInstance(auth.getPrincipal())){
			return (UserDetail)auth.getPrincipal();
		}
		return null;
	}
	
	

}
