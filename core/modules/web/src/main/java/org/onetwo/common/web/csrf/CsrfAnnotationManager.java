package org.onetwo.common.web.csrf;

import java.lang.reflect.Method;
import java.util.concurrent.ConcurrentHashMap;

import org.onetwo.common.utils.AnnotationUtils;
import org.onetwo.common.web.csrf.AbstractCsrfPreventor.CsrfValidInfo;

/***
 * 
 * @author weishao
 *
 */
public class CsrfAnnotationManager  {

	public static final CsrfValidInfo DEFAULT_CSRF = new CsrfValidInfo(true);
	
	private ConcurrentHashMap<String, CsrfValidInfo> caches = new ConcurrentHashMap<String, CsrfValidInfo>();
	
	public boolean isForceValid(Object controller){
		Method method = (Method) controller;
		String key = method.toGenericString();
		CsrfValidInfo csrfInfo = this.caches.get(key);
		if(csrfInfo!=null)
			return csrfInfo.isValid();
		
		CsrfValid csrf = AnnotationUtils.findMethodAnnotationWithStopClass(method, CsrfValid.class);
		if(csrf==null)
			csrfInfo = DEFAULT_CSRF;
		else
			csrfInfo = new CsrfValidInfo(csrf.value());
		this.caches.put(key, csrfInfo);
		return csrfInfo.isValid();
	}
}
