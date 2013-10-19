package org.onetwo.common.web.csrf;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.onetwo.common.web.utils.WebContextUtils;

/****
 * every form generate a new fieldName and value
 * @author wayshall
 *
 */
public class SessionStoreCsrfPreventor extends AbstractCsrfPreventor {
	
	public SessionStoreCsrfPreventor(){
	}
	
	/***
	 * 获取已保存的token值
	 * @param tokenFieldName
	 * @param request
	 * @param response
	 * @return
	 */
	@Override
	protected Token getStoredTokenValue(String tokenFieldName, HttpServletRequest request, HttpServletResponse response){
		return WebContextUtils.getAttr(request.getSession(), tokenFieldName);
	}
	@Override
	protected void cleanStoredTokenValue(Token token, HttpServletRequest request, HttpServletResponse response){
		WebContextUtils.remove(request.getSession(), token.fieldName);
	}

	@Override
	protected void storeToken(Token token, HttpServletRequest request, HttpServletResponse response){
		WebContextUtils.attr(request.getSession(), token.fieldName, token);
	}
}
