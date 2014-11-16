package org.onetwo.common.spring.web.mvc;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.onetwo.common.profiling.UtilTimerStack;
import org.onetwo.common.spring.web.WebHelper;
import org.onetwo.common.spring.web.reqvalidator.JFishRequestValidator;
import org.onetwo.common.spring.web.utils.JFishWebUtils;
import org.onetwo.common.utils.FileUtils;
import org.onetwo.common.utils.LangUtils;
import org.onetwo.common.utils.NiceDate;
import org.onetwo.common.web.config.BaseSiteConfig;
import org.onetwo.common.web.preventor.PreventorFactory;
import org.onetwo.common.web.preventor.RequestPreventor;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.UrlPathHelper;
import org.springframework.web.util.WebUtils;

public class JFishFirstInterceptor extends WebInterceptorAdapter  {
//	private final Logger logger = MyLoggerFactory.getLogger(this.getClass());
	
	private static final UrlPathHelper urlPathHelper = new UrlPathHelper();
	private static final String CONTROLLER_TIME_KEY = "mvc execute";
	public static final String NOW_KEY = "now";
	
	private final List<JFishRequestValidator> requestValidators;
	private RequestPreventor submitPreventor = PreventorFactory.getRepeateSubmitPreventor();
	
	

	public JFishFirstInterceptor(List<JFishRequestValidator> requestValidators) {
		this.requestValidators = requestValidators;
	}

	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		if(!isMethodHandler(handler))
			return true;

		UtilTimerStack.push(CONTROLLER_TIME_KEY);
		
		WebHelper helper = JFishWebUtils.webHelper(request);
		String requestUri = urlPathHelper.getLookupPathForRequest(request);
		String reqUri = WebUtils.extractFullFilenameFromUrlPath(requestUri);
		String extension = FileUtils.getExtendName(reqUri);
		helper.setRequestExtension(extension);
		helper.setControllerHandler(handler);
		

		HandlerMethod hm = getHandlerMethod(handler);
		
		if(BaseSiteConfig.getInstance().isPreventRepeateSubmit()){
			this.submitPreventor.validateToken(hm.getMethod(), request, response);
		}
		
		this.validateRequest(request, response, hm);
		
		/*if(!csrfPreventor.validateToken(request, response)){
			throw new JFishInvalidTokenException();
		}*/
		if(BeforeMethodHandler.class.isInstance(hm.getBean())){
			((BeforeMethodHandler)hm.getBean()).beforeHandler(request, hm.getMethod());
		}
		
		return true;
	}
	
	protected void validateRequest(HttpServletRequest request, HttpServletResponse response, HandlerMethod handler){
		if(LangUtils.isEmpty(requestValidators))
			return ;
		for(JFishRequestValidator v : requestValidators){
			if(v.isSupport(request, handler))
				v.doValidate(request, response, handler);
		}
	}

//	private final static String TYPE_MISMATCH = "typeMismatch";
	
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
		if(modelAndView!=null){
			modelAndView.addObject(NOW_KEY, new NiceDate());
		}else{
			request.setAttribute(NOW_KEY, new NiceDate());
		}
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
