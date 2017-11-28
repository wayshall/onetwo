package org.onetwo.boot.core.jwt;

import java.util.Optional;

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
	@Autowired
	private JwtTokenService jwtTokenService;
	
	private String authHeaderName = JwtUtils.DEFAULT_HEADER_KEY;
	
	public JwtSessionUserManager(String authHeaderName) {
		super();
		this.authHeaderName = authHeaderName;
	}

	@Override
	public JwtUserDetail getCurrentUser() {
//		JwtUserDetail user = (JwtUserDetail)request.getAttribute(JwtUtils.AUTH_ATTR_KEY);
		Optional<JwtUserDetail> userOpt = JwtUtils.getOrSetJwtUserDetail(request, jwtTokenService, authHeaderName);
		return userOpt.orElse(null);
	}

}
