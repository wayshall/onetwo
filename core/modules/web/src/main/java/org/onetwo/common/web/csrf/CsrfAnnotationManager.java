package org.onetwo.common.web.csrf;

import java.lang.reflect.Method;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.http.HttpServletRequest;

import org.onetwo.common.utils.AnnotationUtils;
import org.onetwo.common.web.csrf.AbstractCsrfPreventor.CsrfValidInfo;

/***
 * 
 * @author weishao
 *
 */
public class CsrfAnnotationManager  {

	public static final CsrfValidInfo CSRF_TRUE = new CsrfValidInfo(true);
	public static final CsrfValidInfo CSRF_FALSE = new CsrfValidInfo(false);
	public static final String MEHTOD_GET = "get";
	
	private ConcurrentHashMap<String, CsrfValidInfo> caches = new ConcurrentHashMap<String, CsrfValidInfo>();
	
	public CsrfValidInfo getControllerCsrfInfo(Object controller, HttpServletRequest request){
		Method method = (Method) controller;
		String key = method.toGenericString();
		CsrfValidInfo csrfInfo = this.caches.get(key);
		if(csrfInfo!=null)
			return csrfInfo;
		
		CsrfValid csrf = AnnotationUtils.findMethodAnnotationWithStopClass(method, CsrfValid.class);
		if(csrf==null){
			if(MEHTOD_GET.equalsIgnoreCase(request.getMethod()))
				csrfInfo = CSRF_FALSE;
			else
				csrfInfo = CSRF_TRUE;
		}else{
			csrfInfo = csrf.value()?CSRF_TRUE:CSRF_FALSE;
		}
		this.caches.put(key, csrfInfo);
		return csrfInfo;
	}
}
