package org.onetwo.common.spring.web.authentic;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.onetwo.common.log.MyLoggerFactory;
import org.onetwo.common.utils.ArrayUtils;
import org.onetwo.common.utils.RoleDetail;
import org.onetwo.common.utils.SsoTokenable;
import org.onetwo.common.utils.UserDetail;
import org.onetwo.common.web.s2.security.SecurityTarget;
import org.onetwo.common.web.utils.RequestUtils;
import org.onetwo.common.web.utils.ResponseUtils;
import org.onetwo.common.web.utils.WebContextUtils;
import org.slf4j.Logger;
import org.springframework.web.method.HandlerMethod;

public class SpringSecurityTarget implements SecurityTarget {

	public static SpringSecurityTarget New(HttpServletRequest request, HttpServletResponse response) {
		return new SpringSecurityTarget(request, response);
	}
	
	protected final Logger logger = MyLoggerFactory.getLogger(this.getClass());

	protected HandlerMethod handler;
	protected HttpServletRequest request;
	protected HttpServletResponse response;

//	protected String[] roles;
	protected String sessionKey = UserDetail.USER_DETAIL_KEY ;
	protected String tokenKey = SsoTokenable.TOKEN_KEY;
	
	protected Map<String, Object> datas;
	

	public SpringSecurityTarget(HttpServletRequest request, HttpServletResponse response) {
		this(request, response, null);
	}

	public SpringSecurityTarget(HttpServletRequest request, HttpServletResponse response, HandlerMethod handler) {
		super();
		this.request = request;
		this.response = response;
		this.handler = handler;
	}

//	@Override
//	public Object execute() throws Exception {
//		return null;
//	}

	@Override
	public SecurityTarget addMessage(String msg) {
		return null;
	}

	@Override
	public HandlerMethod getInvocation() {
		return handler;
	}

	@Override
	public UserDetail getAuthoritable() {
		return WebContextUtils.getAttr(request.getSession(), sessionKey);
	}

	@Override
	public void removeCurrentLoginUser() {
		WebContextUtils.remove(request.getSession(), sessionKey);
	}

	@Override
	public void setCurrentLoginUser(UserDetail userDetail) {
		WebContextUtils.attr(request.getSession(), sessionKey, userDetail);
	}

	/****
	 * 如果token不是放在cookie里，直接返回session对象里的token
	 */
	@Override
	public String getCookieToken() {
		return RequestUtils.getCookieValue(request, tokenKey);
	}

	@Override
	public void removeCookieToken() {
		ResponseUtils.removeHttpOnlyCookie(response, tokenKey);
	}

	@Override
	public void setCookieToken(String token) {
		ResponseUtils.setHttpOnlyCookie(response, tokenKey, token);
	}

	public List<String> getRoles() {
		UserDetail userDetail = getAuthoritable();
		if(RoleDetail.class.isInstance(userDetail)){
			return ((RoleDetail) userDetail).getRoles();
		}
		return null;
	}
/*
	public void setRoles(String... roles) {
		this.roles = roles;
	}*/
	
	public boolean containsRole(String role){
		return getRoles()!=null && getRoles().contains(role);
	}

	public Map<String, Object> getDatas() {
		return datas;
	}
	
	public void putData(String key, Object value){
		if(datas==null){
			datas = new HashMap<String, Object>(4);
		}
		datas.put(key, value);
	}
	
	public Object getData(String key){
		if(datas==null)
			return null;
		return datas.get(key);
	}
	
}
