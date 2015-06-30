package org.onetwo.boot.core.web.mvc.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.onetwo.boot.core.web.utils.BootWebHelper;
import org.onetwo.boot.core.web.utils.BootWebUtils;
import org.onetwo.common.profiling.UtilTimerStack;
import org.onetwo.common.utils.FileUtils;
import org.onetwo.common.utils.NiceDate;
import org.springframework.core.Ordered;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.UrlPathHelper;
import org.springframework.web.util.WebUtils;

public class BootFirstInterceptor extends WebInterceptorAdapter {
	
	private static final UrlPathHelper urlPathHelper = new UrlPathHelper();
	private static final String CONTROLLER_TIME_KEY = "mvc execute";
	public static final String NOW_KEY = "now";
	
//	private final List<JFishRequestValidator> requestValidators;
//	private RequestPreventor submitPreventor = PreventorFactory.getRepeateSubmitPreventor();
	
	
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		HandlerMethod handlerMethod = getHandlerMethod(handler);
		if(handlerMethod==null)
			return true;

		UtilTimerStack.push(CONTROLLER_TIME_KEY);
		
		BootWebHelper helper = BootWebUtils.webHelper(request);
		String requestUri = urlPathHelper.getLookupPathForRequest(request);
		String reqUri = WebUtils.extractFullFilenameFromUrlPath(requestUri);
		String extension = FileUtils.getExtendName(reqUri);
		helper.setRequestExtension(extension);
		helper.setControllerHandler(handlerMethod);
		
		/*if(BaseSiteConfig.getInstance().isPreventRepeateSubmit()){
			this.submitPreventor.validateToken(hm.getMethod(), request, response);
		}*/
		
//		this.validateRequest(request, response, hm);
		
		/*if(BeforeMethodHandler.class.isInstance(hm.getBean())){
			((BeforeMethodHandler)hm.getBean()).beforeHandler(request, hm.getMethod());
		}*/
		
		return true;
	}
	
	
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
		if(!isMethodHandler(handler))
			return ;
		
		if(modelAndView!=null){
			modelAndView.addObject(NOW_KEY, new NiceDate());
		}else{
			request.setAttribute(NOW_KEY, new NiceDate());
		}
	}
	

	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
		if(!isMethodHandler(handler))
			return ;
		UtilTimerStack.pop(CONTROLLER_TIME_KEY);
	}

	@Override
	public int getOrder() {
		return Ordered.HIGHEST_PRECEDENCE;
	}
}
