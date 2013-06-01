package org.onetwo.common.web.s2.security;

import org.onetwo.common.utils.UserDetail;
import org.onetwo.common.web.s2.BaseAction;
import org.onetwo.common.web.utils.CookieUtil;
import org.onetwo.common.web.utils.StrutsUtils;

import com.opensymphony.xwork2.ActionInvocation;

public class StrutsSecurityTarget extends SecurityTargetAdaptor{
	
	protected ActionInvocation invocation;
	
	public StrutsSecurityTarget(ActionInvocation invocation){
		this.invocation = invocation;
	}
	
//	public String execute() throws Exception{
//			return invocation.invoke();
//	}
	
	public BaseAction getAction(){
		Object action = this.invocation.getAction();
		if(action instanceof BaseAction)
			return (BaseAction)action;
		return null;
	}

	@Override
	public SecurityTarget addMessage(String msg) {
		this.getAction().addActionMessage(msg);
		return this;
	}

	public ActionInvocation getInvocation() {
		return invocation;
	}

	@Override
	public UserDetail getAuthoritable() {
		return StrutsUtils.getCurrentLoginUser();
	}

	@Override
	public String getCookieToken() {
		return CookieUtil.getCookieToken();
	}

	@Override
	public void removeCurrentLoginUser() {
		StrutsUtils.removeCurrentLoginUser();
	}

	@Override
	public void setCurrentLoginUser(UserDetail userDetail) {
		StrutsUtils.setCurrentLoginUser(userDetail);
	}

	@Override
	public void removeCookieToken() {
		CookieUtil.removeCookieToken();
	}

	@Override
	public void setCookieToken(String token) {
		CookieUtil.setCookieToken(token);
	}

}
