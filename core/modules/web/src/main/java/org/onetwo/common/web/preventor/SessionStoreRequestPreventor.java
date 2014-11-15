package org.onetwo.common.web.preventor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.onetwo.common.web.utils.WebContextUtils;

/****
 * every form generate a new fieldName and value
 * @author wayshall
 *
 */
public class SessionStoreRequestPreventor extends AbstractRequestPreventor {
	
	
	public SessionStoreRequestPreventor(String tokenFieldName){
		super(tokenFieldName);
	}

	/***
	 * 获取已保存的token值
	 * @param tokenFieldName
	 * @param request
	 * @param response
	 * @return
	 */
	@Override
	protected RequestToken getStoredTokenValue(String tokenFieldName, HttpServletRequest request, HttpServletResponse response){
		return WebContextUtils.getAttr(request.getSession(), tokenFieldName);
	}
	@Override
	protected void cleanStoredTokenValue(boolean invalid, RequestToken token, HttpServletRequest request, HttpServletResponse response){
		WebContextUtils.remove(request.getSession(), token.getFieldName());
	}

	@Override
	protected void storeToken(RequestToken token, HttpServletRequest request, HttpServletResponse response){
		WebContextUtils.attr(request.getSession(), token.getFieldName(), token);
	}
}
