package org.onetwo.common.web.s2.security;

import org.apache.commons.lang.StringUtils;

import com.opensymphony.xwork2.ActionContext;


public class SecurityUtils {

	public static final String SECURITY_RESULT = "security_result";
	
	private SecurityUtils(){
	}
	
	public static void setCurrentSecurityResult(String result){
		ActionContext.getContext().put(SECURITY_RESULT, result);
	}
	
	public static String getCurrentSecurityResult(String defResult){
		String rsView = (String)ActionContext.getContext().get(SECURITY_RESULT);
		if(StringUtils.isBlank(rsView)){
			rsView = defResult;
			setCurrentSecurityResult(rsView);
//			invocation.getInvocationContext().getValueStack().getContext().put(SECURITY_RESULT, StrutsAuthentication.LONGIN_VIEW);
		}
		return rsView;
	}

}
