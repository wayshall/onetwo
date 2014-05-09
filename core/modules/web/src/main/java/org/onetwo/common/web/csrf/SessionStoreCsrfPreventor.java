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
	
	private CsrfAnnotationManager csrfAnnotationManager;
	
	public SessionStoreCsrfPreventor(){
		this.csrfAnnotationManager = new CsrfAnnotationManager();
	}
	
	protected boolean isValidCsrf(Object controller, HttpServletRequest request){
		return csrfAnnotationManager.getControllerCsrfInfo(controller, request).isValid();
	}
	
	/***
	 * 获取已保存的token值
	 * @param tokenFieldName
	 * @param request
	 * @param response
	 * @return
	 */
	@Override
	protected CsrfToken getStoredTokenValue(String tokenFieldName, HttpServletRequest request, HttpServletResponse response){
		return WebContextUtils.getAttr(request.getSession(), tokenFieldName);
	}
	@Override
	protected void cleanStoredTokenValue(boolean invalid, CsrfToken token, HttpServletRequest request, HttpServletResponse response){
		WebContextUtils.remove(request.getSession(), token.getFieldName());
	}

	@Override
	protected void storeToken(CsrfToken token, HttpServletRequest request, HttpServletResponse response){
		WebContextUtils.attr(request.getSession(), token.getFieldName(), token);
	}
}
