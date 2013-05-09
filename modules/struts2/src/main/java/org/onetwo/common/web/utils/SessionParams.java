package org.onetwo.common.web.utils;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.onetwo.common.utils.UserDetail;

/*****
 * 对session操作的封装
 * 
 * @author weishao
 *
 */
@SuppressWarnings("unchecked")
public class SessionParams {

	protected static Logger logger = Logger.getLogger(SessionParams.class);

	protected HttpSession session;
	
	protected String userDetailKey;

	public SessionParams() {
	}

	public SessionParams(HttpServletRequest request) {
		session = request.getSession(false);
	}

	public SessionParams(HttpSession session) {
		this.session = session;
	}

	public static void updateCurrentUserLastActivityTime(HttpServletRequest request, Date date) {
		SessionParams s = new SessionParams(request);
		if(date==null)
			date = new Date();
		s.updateLastActivityTime(date);
	}

	public static void updateCurrentUserLastActivityTime(Date date) {
		SessionParams s = new SessionParams(StrutsUtils.getRequest());
		s.updateLastActivityTime(date);
	}

	public void updateLastActivityTime(Date date) {
		try {
			UserDetail user = this.getUserDetail();
			if (user == null)
				return;
			if (date == null)
				date = new Date();
			user.setLastActivityTime(date);
		} catch (Exception e) {
			logger.error("update LastActivityTime error. ", e);
		}
	}

	public SessionParams setAttribute(String key, Object value) {
		session.setAttribute(key, value);
		return this;
	}

	public Object getAttribute(String key) {
		return session.getAttribute(key);
	}

	public <T> T getAttribute(String key, Class<T> clazz) {
		return (T) session.getAttribute(key);
	}

	public Object removeAttribute(String key) {
		return SessionUtils.remove(session, key);
	}

	/**
	 * 把用户信息放入session
	 * 
	 * @param aRequest
	 * @param userlogininfo
	 * @author linxiaoyu 2009-3-11
	 */
	public void setUserDetail(UserDetail userDetail) {
		SessionUtils.setUserDetail(session, userDetail);
	}

	public UserDetail getUserDetail() {
		return SessionUtils.getUserDetail(session);
	}

	public void removeUserDetail() {
		SessionUtils.removeUserDetail(session);
	}

}