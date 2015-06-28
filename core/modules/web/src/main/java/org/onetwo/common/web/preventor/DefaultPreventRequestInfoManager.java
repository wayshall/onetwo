package org.onetwo.common.web.preventor;

import java.lang.reflect.Method;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.onetwo.common.exception.BaseException;
import org.onetwo.common.utils.AnnotationUtils;
import org.onetwo.common.web.csrf.CsrfValid;
import org.onetwo.common.web.utils.RequestUtils;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.collect.ImmutableMap;

/***
 * 
 * @author weishao
 *
 */
public class DefaultPreventRequestInfoManager implements PreventRequestInfoManager  {

//	public static final RequestValidateInfo CSRF_TRUE = RequestValidateInfo.TRUE;
//	public static final RequestValidateInfo CSRF_FALSE = RequestValidateInfo.FALSE;
	public static final String MEHTOD_GET = RequestPreventor.MEHTOD_GET;
	
	private static final RequestPreventInfo NO_REPEATESUBMIT_NO_CSRF = new RequestPreventInfo(false, false);
	private static final RequestPreventInfo REPEATESUBMIT_NO_CSRF = new RequestPreventInfo(true, false);
	private static final RequestPreventInfo NO_REPEATESUBMIT_CSRF = new RequestPreventInfo(false, true);
	private static final RequestPreventInfo REPEATESUBMIT_CSRF = new RequestPreventInfo(true, true);
	private final Map<String, RequestPreventInfo> preventMap;
	
	{
		preventMap = ImmutableMap.of(NO_REPEATESUBMIT_NO_CSRF.getKey(), NO_REPEATESUBMIT_NO_CSRF,
									REPEATESUBMIT_NO_CSRF.getKey(), REPEATESUBMIT_NO_CSRF,
									NO_REPEATESUBMIT_CSRF.getKey(), NO_REPEATESUBMIT_CSRF,
									REPEATESUBMIT_CSRF.getKey(), REPEATESUBMIT_CSRF);
	}
	
	
//	private ConcurrentHashMap<String, RequestValidateInfo> caches = new ConcurrentHashMap<String, RequestValidateInfo>();
	private Cache<String, RequestPreventInfo> caches = CacheBuilder.newBuilder().build();
	private boolean preventRepeateSubmitDefault = false;
	/* (non-Javadoc)
	 * @see org.onetwo.common.web.preventor.PreventRequestInfoManager#getRequestPreventInfo(java.lang.reflect.Method, javax.servlet.http.HttpServletRequest)
	 */
	@Override
	public RequestPreventInfo getRequestPreventInfo(Method controller, HttpServletRequest request){
		if(controller==null || MEHTOD_GET.equalsIgnoreCase(request.getMethod()))
			return NO_REPEATESUBMIT_NO_CSRF;

		String key = request.getMethod()+"|"+RequestUtils.getServletPath(request);
		RequestPreventInfo preventRequestInfo = this.caches.getIfPresent(key);
		if(preventRequestInfo!=null)
			return preventRequestInfo;

		Method method = controller;
		PreventRequest preventRequest = AnnotationUtils.findMethodAnnotationWithStopClass(method, PreventRequest.class);
		if(preventRequest==null){
			CsrfValid csrf = AnnotationUtils.findMethodAnnotationWithStopClass(method, CsrfValid.class);
			if(csrf==null){
				//当csrf启用后，默认检查csrf
				
				//TODO: 这里应该是读取spring requestMapping的method来判断，如果是非get方法，强制验证
				/*if(MEHTOD_GET.equalsIgnoreCase(request.getMethod()))
					preventRequestInfo = NO_REPEATESUBMIT_NO_CSRF;
				else
					preventRequestInfo = NO_REPEATESUBMIT_CSRF;*/
//				return REPEATESUBMIT_CSRF;
//				if(BaseSiteConfig.getInstance().isPreventRepeateSubmitDefault()){
				if(preventRepeateSubmitDefault){
					return REPEATESUBMIT_CSRF;
				}else{
					return NO_REPEATESUBMIT_CSRF;
				}
			}else{
				preventRequestInfo = csrf.value()?NO_REPEATESUBMIT_CSRF:NO_REPEATESUBMIT_NO_CSRF;
			}
		}else{
			String rs = preventRequest.repeateSubmit()+"-"+preventRequest.csrf();
			preventRequestInfo = preventMap.get(rs);
			if(preventRequestInfo==null)
				throw new BaseException("it's impossible, the code is error!");
		}
		
		this.caches.put(key, preventRequestInfo);
		return preventRequestInfo;
	}
}
