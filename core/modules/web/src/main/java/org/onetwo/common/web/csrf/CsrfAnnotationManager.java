package org.onetwo.common.web.csrf;


/***
 * 
 * @author weishao
 *
 */
public class CsrfAnnotationManager  {

	/*public static final RequestValidateInfo CSRF_TRUE = RequestValidateInfo.TRUE;
	public static final RequestValidateInfo CSRF_FALSE = RequestValidateInfo.FALSE;
	public static final String MEHTOD_GET = RequestPreventor.MEHTOD_GET;
	
	private ConcurrentHashMap<String, RequestValidateInfo> caches = new ConcurrentHashMap<String, RequestValidateInfo>();
	
	public RequestValidateInfo getControllerCsrfInfo(Method controller, HttpServletRequest request){
		if(MEHTOD_GET.equalsIgnoreCase(request.getMethod()))
			return CSRF_FALSE;

		String key = request.getMethod()+"|"+RequestUtils.getServletPath(request);
		RequestValidateInfo csrfInfo = this.caches.get(key);
		if(csrfInfo!=null)
			return csrfInfo;

		if(controller==null)
			return CSRF_FALSE;
		
		Method method = controller;
		CsrfValid csrf = AnnotationUtils.findMethodAnnotationWithStopClass(method, CsrfValid.class);
		if(csrf==null){
			//TODO: 这里应该是读取spring requestMapping的method来判断，如果是非get方法，强制验证
			if(MEHTOD_GET.equalsIgnoreCase(request.getMethod()))
				csrfInfo = CSRF_FALSE;
			else
				csrfInfo = CSRF_TRUE;
		}else{
			csrfInfo = csrf.value()?CSRF_TRUE:CSRF_FALSE;
		}
		this.caches.put(key, csrfInfo);
		return csrfInfo;
	}*/
}
