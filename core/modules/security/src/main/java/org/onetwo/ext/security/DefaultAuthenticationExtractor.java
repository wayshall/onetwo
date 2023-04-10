package org.onetwo.ext.security;

import org.onetwo.common.web.userdetails.GenericUserDetail;
import org.onetwo.common.web.userdetails.SimpleUserDetail;
import org.onetwo.ext.security.jwt.SecurityJwtUserDetail;
import org.springframework.security.core.Authentication;

public class DefaultAuthenticationExtractor implements AuthenticationExtractor {

	@Override
	public Object extract(Authentication authentication) {
		if (!(authentication.getPrincipal() instanceof GenericUserDetail)) {
			return authentication.getPrincipal(); 
		}
		// see SecurityJwtUserDetail
		GenericUserDetail<?> user = (GenericUserDetail<?>)authentication.getPrincipal();
		SimpleUserDetail userDetail = new SimpleUserDetail();
		userDetail.setUserId((Long)user.getUserId());
		userDetail.setUserName(user.getUserName());
//		userDetail.setUserType(null);
		if (user instanceof SecurityJwtUserDetail) {
			SecurityJwtUserDetail detail = (SecurityJwtUserDetail) user;
			userDetail.setAdminRole(SimpleUserDetail.ADMIN_ROLES.contains(detail.getRoles()));
		}
		return userDetail;
	}

}
