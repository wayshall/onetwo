package org.onetwo.common.web.csrf;

import java.lang.reflect.Method;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.http.HttpServletRequest;

import org.onetwo.common.utils.AnnotationUtils;
import org.onetwo.common.web.preventor.RequestPreventor;
import org.onetwo.common.web.preventor.AbstractRequestPreventor.SubmitValidInfo;
import org.onetwo.common.web.utils.RequestUtils;

/***
 * 
 * @author weishao
 *
 */
public class CsrfAnnotationManager  {

	public static final SubmitValidInfo CSRF_TRUE = new SubmitValidInfo(true);
	public static final SubmitValidInfo CSRF_FALSE = new SubmitValidInfo(false);
	public static final String MEHTOD_GET = RequestPreventor.MEHTOD_GET;
	
	private ConcurrentHashMap<String, SubmitValidInfo> caches = new ConcurrentHashMap<String, SubmitValidInfo>();
	
	public SubmitValidInfo getControllerCsrfInfo(Object controller, HttpServletRequest request){
		if(MEHTOD_GET.equalsIgnoreCase(request.getMethod()))
			return CSRF_FALSE;

		String key = request.getMethod()+"|"+RequestUtils.getServletPath(request);
		SubmitValidInfo csrfInfo = this.caches.get(key);
		if(csrfInfo!=null)
			return csrfInfo;

		if(controller==null)
			return CSRF_FALSE;
		
		Method method = (Method) controller;
		CsrfValid csrf = AnnotationUtils.findMethodAnnotationWithStopClass(method, CsrfValid.class);
		if(csrf==null){
			//TODO: 这里应该是读取spring requestMapping的method来判断
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
