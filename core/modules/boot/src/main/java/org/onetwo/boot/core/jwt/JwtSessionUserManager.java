package org.onetwo.boot.core.jwt;

import javax.servlet.http.HttpServletRequest;

import org.onetwo.common.web.userdetails.SessionUserManager;
import org.onetwo.common.web.userdetails.UserDetail;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author wayshall
 * <br/>
 */
public class JwtSessionUserManager implements SessionUserManager<UserDetail> {
	
	@Autowired
	private HttpServletRequest request;

	@Override
	public JwtUserDetail getCurrentUser() {
		JwtUserDetail user = (JwtUserDetail)request.getAttribute(JwtUtils.AUTH_ATTR_KEY);
		return user;
	}
	

}
