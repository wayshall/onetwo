package org.onetwo.common.web.preventor;

import java.lang.reflect.Method;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class SessionRepeateSubmitPreventor extends SessionStoreRequestPreventor {
	private static final String DEFAULT_CSRF_TOKEN_FIELD = "_jfish.submit.token";

	private PreventRequestInfoManager preventRequestInfoManager;
	
	public SessionRepeateSubmitPreventor(PreventRequestInfoManager preventRequestInfoManager) {
		super(DEFAULT_CSRF_TOKEN_FIELD);
		this.preventRequestInfoManager = preventRequestInfoManager;
	}

	public boolean isValidateToken(Method controller, HttpServletRequest request){
		return preventRequestInfoManager.getRequestPreventInfo(controller, request).isRepeateSubmitValidate();
	}

	protected void handleInvalidToken(RequestToken token, HttpServletRequest request, HttpServletResponse response){
		cleanStoredTokenValue(true, token, request, response);
		throw new IllegalRequestException("你已提交过请求，请稍候，勿反复提交！");
	}
}
