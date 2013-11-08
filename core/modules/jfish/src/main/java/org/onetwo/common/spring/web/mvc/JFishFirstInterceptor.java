package org.onetwo.common.spring.web.mvc;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.onetwo.common.profiling.UtilTimerStack;
import org.onetwo.common.spring.web.WebHelper;
import org.onetwo.common.spring.web.utils.JFishWebUtils;
import org.onetwo.common.utils.FileUtils;
import org.onetwo.common.utils.NiceDate;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.UrlPathHelper;
import org.springframework.web.util.WebUtils;

public class JFishFirstInterceptor extends WebInterceptorAdapter  {
//	private final Logger logger = MyLoggerFactory.getLogger(this.getClass());
	
	private static final UrlPathHelper urlPathHelper = new UrlPathHelper();
	private static final String CONTROLLER_TIME_KEY = "controller execute";

	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		if(!isMethodHandler(handler))
			return true;

		UtilTimerStack.push(CONTROLLER_TIME_KEY);
		
		request.setAttribute("now", new NiceDate());
		WebHelper helper = JFishWebUtils.webHelper(request);
		String requestUri = urlPathHelper.getLookupPathForRequest(request);
		String reqUri = WebUtils.extractFullFilenameFromUrlPath(requestUri);
		String extension = FileUtils.getExtendName(reqUri);
		helper.setRequestExtension(extension);
		helper.setControllerHandler(handler);

		HandlerMethod hm = getHandlerMethod(handler);
		
		/*if(!csrfPreventor.validateToken(request, response)){
			throw new JFishInvalidTokenException();
		}*/
		if(BeforeMethodHandler.class.isInstance(hm.getBean())){
			((BeforeMethodHandler)hm.getBean()).beforeHandler(hm.getMethod());
		}
		
		return true;
	}

//	private final static String TYPE_MISMATCH = "typeMismatch";
	
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
		/*if(modelAndView!=null && modelAndView.getModelMap()!=null){
			BeanPropertyBindingResult br = null;
			for(Map.Entry<String, Object> entry : modelAndView.getModelMap().entrySet()){
				if(entry.getValue() instanceof BeanPropertyBindingResult){
					br = (BeanPropertyBindingResult) entry.getValue();
					break;
				}
			}
			if(br!=null){
				List<FieldError> fields = new ArrayList<FieldError>(br.getFieldErrors());
				for(FieldError fe : fields){
					if(ArrayUtils.contains(fe.getCodes(), TYPE_MISMATCH)){
						String msg = SpringApplication.getInstance().getBean(CodeMessager.class).getMessage(TYPE_MISMATCH);
						br.rejectValue(fe.getField(), TYPE_MISMATCH, msg);
						break;
					}
				}
			}
		}*/
	}
	

	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
		UtilTimerStack.pop(CONTROLLER_TIME_KEY);
	}

	@Override
	public int getOrder() {
		return InterceptorOrder.FIRST;
	}
}
