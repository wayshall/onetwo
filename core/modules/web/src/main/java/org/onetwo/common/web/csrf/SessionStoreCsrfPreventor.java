package org.onetwo.common.web.csrf;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.onetwo.common.utils.StringUtils;
import org.onetwo.common.web.utils.WebContextUtils;

public class SessionStoreCsrfPreventor extends CsrfPreventor {
	
	public SessionStoreCsrfPreventor(){
	}
	
	/***
	 * 获取token域的名称
	 * @param request
	 * @param response
	 * @return
	 */
	@Override
	protected String getTokenFieldName(HttpServletRequest request, HttpServletResponse response){
		String tokenName = request.getParameter(fieldOfTokenFieldName);
		return StringUtils.isBlank(tokenName)?fieldOfTokenFieldName:tokenName;
	}
	/***
	 * 获取已保存的token值
	 * @param tokenFieldName
	 * @param request
	 * @param response
	 * @return
	 */
	@Override
	protected String getStoredTokenValue(String tokenFieldName, HttpServletRequest request, HttpServletResponse response){
		return WebContextUtils.getAttr(request.getSession(), tokenFieldName);
	}
	@Override
	protected void cleanStoredTokenValue(String tokenFieldName, HttpServletRequest request, HttpServletResponse response){
		WebContextUtils.remove(request.getSession(), tokenFieldName);
	}

	@Override
	protected void storeToken(Token token, HttpServletRequest request, HttpServletResponse response){
		WebContextUtils.attr(request.getSession(), token.fieldName, token.value);
	}
}
