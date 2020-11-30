package org.onetwo.boot.core.web.socket;

import java.security.Principal;
import java.util.Map;

import org.onetwo.common.web.userdetails.SessionUserManager;
import org.onetwo.common.web.userdetails.UserDetail;
import org.onetwo.common.web.userdetails.UserDetailPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;

public class PrincipalHandshakeHandler extends DefaultHandshakeHandler {
	
	@Autowired
	SessionUserManager<?> sessionUserManager;

	@Override
	protected Principal determineUser(ServerHttpRequest request, WebSocketHandler wsHandler, Map<String, Object> attributes) {
		UserDetail user = (UserDetail)sessionUserManager.getCurrentUser();
		if (user==null) {
			return null;
		}
		return new UserDetailPrincipal(user.getUserId(), user.getUserName());
	}

}
